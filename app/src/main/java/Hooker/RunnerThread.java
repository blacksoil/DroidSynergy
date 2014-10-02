package Hooker;

import android.util.Log;

import com.blacksoil.droidsynergy.Logger;
import com.blacksoil.droidsynergy.connection.ConnectionCallbackInterface;
import com.blacksoil.droidsynergy.connection.ConnectionInterface;
import com.blacksoil.droidsynergy.connection.StreamConnection;
import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.input.SimpleInput;
import com.blacksoil.droidsynergy.packet.CClipboardPacket;
import com.blacksoil.droidsynergy.packet.DClipboardPacket;
import com.blacksoil.droidsynergy.packet.EnterScreenPacket;
import com.blacksoil.droidsynergy.packet.ExitScreenPacket;
import com.blacksoil.droidsynergy.packet.HandshakePacket;
import com.blacksoil.droidsynergy.packet.InfoAcknowledgmentPacket;
import com.blacksoil.droidsynergy.packet.KeepAlivePacket;
import com.blacksoil.droidsynergy.packet.KeyPressDnPacket;
import com.blacksoil.droidsynergy.packet.KeyPressRpPacket;
import com.blacksoil.droidsynergy.packet.KeyPressUpPacket;
import com.blacksoil.droidsynergy.packet.MouseDownPacket;
import com.blacksoil.droidsynergy.packet.MouseMovePacket;
import com.blacksoil.droidsynergy.packet.MouseUpPacket;
import com.blacksoil.droidsynergy.packet.MouseWheelPacket;
import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.packet.RelativeMovePacket;
import com.blacksoil.droidsynergy.packet.ResetOptionPacket;
import com.blacksoil.droidsynergy.packet.ScreenInfoPacket;
import com.blacksoil.droidsynergy.packet.SetOptionPacket;
import com.blacksoil.droidsynergy.parser.ParserInterface;
import com.blacksoil.droidsynergy.parser.SimpleParser;
import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by aharijanto on 10/1/14.
 */
public class RunnerThread extends Thread implements ConnectionCallbackInterface {
    private static final String TAG = "RunnerThread";
    private static final boolean DEBUG = false;

    // Associate network packet "string identifier"
    // with the Packet object
    private Map<String, Packet> mStringToPacketMap = new HashMap<String, Packet>();


    private String mIpAddr;
    private int mPort;

    private Thread mLooperThread;
    private Thread mNetworkThread;

    // Network connection handler
    private ConnectionInterface mConnection;

    // Packet Queue used to interact with Connection
    private Queue<Packet> mQueue;

    // Callback for Connection
    private ConnectionCallbackInterface mCallback;

    // Network byte parser
    private ParserInterface mParser;

    // Associated Runnable for the Thread above
    private Runnable mNetworkRunnable = new Runnable() {
        public void run() {
            mConnection = new StreamConnection(mIpAddr, mPort, mQueue, mCallback,
                    mParser);
            // Begin listening
            mConnection.beginConnection();
        }
    };

    // Packet poller thread
    // Poll packet from the packet queue every specified time
    private Runnable mLooperRunnable = new Runnable() {
        public void run() {
            // The next packet to be processed
            Packet rcvPacket;
            // Response to be sent to server
            Response response;
            while (mConnection.isConnected()) {
                // Wait until the queue is not empty
                synchronized (mQueue) {
                    if (mQueue.isEmpty()) {
                        try {
                            mQueue.wait();
                        } catch (InterruptedException e) {
                            Logd("mQueue.wait() interrupted!");
                            e.printStackTrace();
                        }
                    }
                    if (DEBUG) Logd("Queue size: " + mConnection.getQueueSize());
                    rcvPacket = mQueue.remove();
                }

                // Log the packet textual description
                if (DEBUG) Logd("Received: " + rcvPacket.getDescription() + "\n");

                response = rcvPacket.generateResponse();
                // Logd(Utility.dump(response));

                // Send the response over the network
                mConnection.writeResponse(response);
            }

            Logd("Looper Thread quits");
        }
    };

    public void Logd(String msg) {
        Log.d(TAG, msg);
    }

    public void Loge(String msg) {
        Log.e(TAG, msg);
    }

    public void Logi(String msg) {

    }


    public RunnerThread(String ipAddr, int port) {
        mIpAddr = ipAddr;
        mPort = port;
        mQueue = new LinkedList<Packet>();
        mCallback = this;

        // This has to be called before passing the map to parser
        initializesAssociationMap();

        mParser = new SimpleParser(mStringToPacketMap);

        mQueue = new LinkedList<Packet>();

        mCallback = this;

        debug();

        // Start the network and packet polling thread
        startThreads();
    }

    // Starts the network and polling thread
    private void startThreads() {
        // Start the connection thread
        mNetworkThread = new Thread(mNetworkRunnable);
        mNetworkThread.start();

        // Don't start the packet poller thread until the connection is made
        // PS: Will be started when isConnected() callback is called
        mLooperThread = new Thread(mLooperRunnable);
    }


    // Silly method used to debug some info
    private void debug() {

    }

    // Initializes the association map
    private void initializesAssociationMap() {
        Packet handShake = new HandshakePacket();
        Packet screenInfo = new ScreenInfoPacket();
        Packet infoAcknowledgment = new InfoAcknowledgmentPacket();
        Packet resetOption = new ResetOptionPacket();
        Packet setOption = new SetOptionPacket();
        Packet keepAlive = new KeepAlivePacket();
        Packet enterScreen = new EnterScreenPacket();
        Packet exitScreen = new ExitScreenPacket();
        Packet cClipboard = new CClipboardPacket();
        Packet dClipboard = new DClipboardPacket();
        Packet mouseMove = new MouseMovePacket();
        Packet relMove = new RelativeMovePacket();
        Packet mouseDown = new MouseDownPacket();
        Packet mouseUp = new MouseUpPacket();
        Packet mouseWheel = new MouseWheelPacket();
        Packet keyPressDn = new KeyPressDnPacket();
        Packet keyPressUp = new KeyPressUpPacket();
        Packet keyPressRp = new KeyPressRpPacket();

        mStringToPacketMap.put(handShake.getType(), handShake);
        mStringToPacketMap.put(screenInfo.getType(), screenInfo);
        mStringToPacketMap
                .put(infoAcknowledgment.getType(), infoAcknowledgment);
        mStringToPacketMap.put(resetOption.getType(), resetOption);
        mStringToPacketMap.put(setOption.getType(), setOption);
        mStringToPacketMap.put(keepAlive.getType(), keepAlive);
        mStringToPacketMap.put(enterScreen.getType(), enterScreen);
        mStringToPacketMap.put(cClipboard.getType(), cClipboard);
        mStringToPacketMap.put(dClipboard.getType(), dClipboard);
        mStringToPacketMap.put(mouseMove.getType(), mouseMove);
        mStringToPacketMap.put(exitScreen.getType(), exitScreen);
        mStringToPacketMap.put(relMove.getType(), relMove);
        mStringToPacketMap.put(mouseDown.getType(), mouseDown);
        mStringToPacketMap.put(mouseUp.getType(), mouseUp);
        mStringToPacketMap.put(mouseWheel.getType(), mouseWheel);
        mStringToPacketMap.put(keyPressDn.getType(), keyPressDn);
        mStringToPacketMap.put(keyPressUp.getType(), keyPressUp);
        mStringToPacketMap.put(keyPressRp.getType(), keyPressRp);
    }


    @Override
    public void interrupt() {
        mLooperThread.interrupt();
        mNetworkThread.interrupt();
        super.interrupt();
    }

    public void connected() {

    }

    public void disconnected() {

    }

    public void error(String msg) {

    }

    public void problem(String msg) {

    }

    public void log(String msg) {

    }
}
