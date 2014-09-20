package com.blacksoil.droidsynergy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.blacksoil.droidsynergy.connection.ConnectionCallbackInterface;
import com.blacksoil.droidsynergy.connection.ConnectionInterface;
import com.blacksoil.droidsynergy.connection.StreamConnection;
import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.input.SimpleInput;
import com.blacksoil.droidsynergy.packet.ClipboardPacket;
import com.blacksoil.droidsynergy.packet.EnterScreenPacket;
import com.blacksoil.droidsynergy.packet.ExitScreenPacket;
import com.blacksoil.droidsynergy.packet.HandshakePacket;
import com.blacksoil.droidsynergy.packet.InfoAcknowledgmentPacket;
import com.blacksoil.droidsynergy.packet.KeepAlivePacket;
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

public class MainActivity extends Activity implements
		ConnectionCallbackInterface, Logger {
	private String mHost = "172.17.185.236";
	private int mPort = 24800;

	// Thread that handles network connections
	private Thread mNetworkThread;
	// Thread that dispatches action for the connection packet
	private Thread mLooperThread;

	// Associate network packet "string identifier"
	// with the Packet object
	private Map<String, Packet> mStringToPacketMap = new HashMap<String, Packet>();

	// Network connection handler
	private ConnectionInterface mConnection;

	// Network byte parser
	private ParserInterface mParser;

	// Packet Queue used to interact with Connection
	private Queue<Packet> mQueue;

	// Callback for Connection
	private ConnectionCallbackInterface mCallback;

	// Logging TAG
	private static String TAG = "DroidSynergy";

	private static boolean DEBUG = true;

	// Associated Runnable for the Thread above
	private Runnable mNetworkRunnable = new Runnable() {
		public void run() {
			mConnection = new StreamConnection(mHost, mPort, mQueue, mCallback,
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializes global logger
		new GlobalLogger(this);

		// Initializes constants required
		DroidSynergyShared.initialize("android", this, new SimpleInput());

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
		Packet clipboard = new ClipboardPacket();
		Packet mouseMove = new MouseMovePacket();
		Packet relMove = new RelativeMovePacket();
		Packet mouseDown = new MouseDownPacket();
		Packet mouseUp = new MouseUpPacket();
		Packet mouseWheel = new MouseWheelPacket();

		mStringToPacketMap.put(handShake.getType(), handShake);
		mStringToPacketMap.put(screenInfo.getType(), screenInfo);
		mStringToPacketMap
				.put(infoAcknowledgment.getType(), infoAcknowledgment);
		mStringToPacketMap.put(resetOption.getType(), resetOption);
		mStringToPacketMap.put(setOption.getType(), setOption);
		mStringToPacketMap.put(keepAlive.getType(), keepAlive);
		mStringToPacketMap.put(enterScreen.getType(), enterScreen);
		mStringToPacketMap.put(clipboard.getType(), clipboard);
		mStringToPacketMap.put(mouseMove.getType(), mouseMove);
		mStringToPacketMap.put(exitScreen.getType(), exitScreen);
		mStringToPacketMap.put(relMove.getType(), relMove);
		mStringToPacketMap.put(mouseDown.getType(), mouseDown);
		mStringToPacketMap.put(mouseUp.getType(), mouseUp);
		mStringToPacketMap.put(mouseWheel.getType(), mouseWheel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void connected() {
		Logd("Connected!");
		mLooperThread.start();
	}

	public void disconnected() {
		Logd("Disconnected!");
		// Sleep for a while
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Reconnect
		startThreads();
		System.exit(1);
	}

	public void error(String msg) {
		Log.e(TAG, msg);
		System.exit(1);
	}

	public void problem(String msg) {
		Logd(msg);
	}

	public void log(String msg) {
		Logd("LOG:" + msg);
	}

	// Log.d wrapper
	public void Logd(String msg) {
		Log.d(TAG, msg);
	}

	// Invoked only for debugging purposes
	public void println(String x) {
		Logd(x);
	}

	public void Loge(String msg) {
		Log.e(TAG, msg);
		System.exit(1);
	}

	public void Logi(String msg) {
		Log.i(TAG, msg);
	}

	// A silly method
	// used to debug
	public void debug() {
		// String str = Utility.dump(new ScreenInfoResponse().toByteArray());
		// Logd(str);
		// Logd("Height: " + DroidSynergyBuild.getInstance().getScreenHeight());
		// Logd("Width: " + DroidSynergyBuild.getInstance().getScreenWidth());
		// System.exit(1);
	}

}
