package com.blacksoil.droidsynergy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.blacksoil.droidsynergy.connection.Connection;
import com.blacksoil.droidsynergy.connection.ConnectionCallback;
import com.blacksoil.droidsynergy.connection.StreamConnection;
import com.blacksoil.droidsynergy.constant.DroidSynergyBuild;
import com.blacksoil.droidsynergy.packet.HandshakePacket;
import com.blacksoil.droidsynergy.packet.InfoAcknowledgmentPacket;
import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.packet.ResetOptionPacket;
import com.blacksoil.droidsynergy.packet.ScreenInfoPacket;
import com.blacksoil.droidsynergy.packet.SetOptionPacket;
import com.blacksoil.droidsynergy.parser.Parser;
import com.blacksoil.droidsynergy.parser.SimpleParser;
import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

public class MainActivity extends Activity implements ConnectionCallback,
		Logger {
	private String mHost = "192.168.1.142";
	private int mPort = 24800;

	// Thread that handles network connections
	private Thread mNetworkThread;
	// Thread that dispatches action for the connection packet
	private Thread mLooperThread;

	// Associate network packet "string identifier"
	// with the Packet object
	private Map<String, Packet> mStringToPacketMap = new HashMap<String, Packet>();

	// Network connection handler
	private Connection mConnection;

	// Network byte parser
	private Parser mParser;

	// Packet Queue used to interact with Connection
	private Queue<Packet> mQueue;

	// Callback for Connection
	private ConnectionCallback mCallback;

	// Logging interface
	private Logger mLogger;

	// Logging TAG
	private static String TAG = "DroidSynergy";

	// Sleep time for each loop in LooperThread in ms
	private static int LOOPER_DELAY = 20;

	// Associated Runnable for the Thread above
	private Runnable mNetworkRunnable = new Runnable() {
		public void run() {
			try {
				mConnection = new StreamConnection(mHost, mPort, mQueue,
						mCallback, mParser);
				// Begin listening
				mConnection.beginConnection();
			} catch (UnknownHostException e) {
				Logd("UnknownHostException: " + e.getLocalizedMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				Logd("IOException: " + e.getLocalizedMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	private Runnable mLooperRunnable = new Runnable() {
		public void run() {
			// The next packet to be processed
			Packet rcvPacket;
			// Response to be sent to server
			Response response;
			while (true) {
				// Grab the next packet from the queue
				if (!mQueue.isEmpty()) {
					rcvPacket = mQueue.remove();
					// Log the packet textual description
					Logd("Received: " + rcvPacket.getDescription());

					response = rcvPacket.generateResponse();
					// Logd(Utility.dump(response));

					// Send the response over the network
					mConnection.writeResponse(response);
				}

				try {
					Thread.sleep(LOOPER_DELAY);
				} catch (InterruptedException e) {
					Logd("Sleep fail on mLooperThread!");
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializes global logger
		new GlobalLogger(this);

		// Initializes constants required
		DroidSynergyBuild.initialize("android", this);

		// This has to be called before passing the map to parser
		initializesAssociationMap();

		mParser = new SimpleParser(mStringToPacketMap);

		mQueue = new LinkedList<Packet>();

		mCallback = this;

		mLogger = this;

		debug();

		// Start the connection thread
		mNetworkThread = new Thread(mNetworkRunnable);
		mNetworkThread.start();

		// Don't run the looper until connection is made
		mLooperThread = new Thread(mLooperRunnable);
	}

	// Initializes the association map
	private void initializesAssociationMap() {
		Packet handShake = new HandshakePacket();
		Packet screenInfo = new ScreenInfoPacket();
		Packet infoAcknowledgment = new InfoAcknowledgmentPacket();
		Packet resetOption = new ResetOptionPacket();
		Packet setOption = new SetOptionPacket();
		
		mStringToPacketMap.put(handShake.getType(), handShake);
		mStringToPacketMap.put(screenInfo.getType(), screenInfo);
		mStringToPacketMap.put(infoAcknowledgment.getType(), infoAcknowledgment);
		mStringToPacketMap.put(resetOption.getType(), resetOption);
		mStringToPacketMap.put(setOption.getType(), setOption);
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
