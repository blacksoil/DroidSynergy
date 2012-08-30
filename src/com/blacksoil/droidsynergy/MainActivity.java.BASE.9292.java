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
import com.blacksoil.droidsynergy.packet.HandshakePacket;
import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.parser.Parser;
import com.blacksoil.droidsynergy.parser.SimpleParser;

public class MainActivity extends Activity implements ConnectionCallback {
	private String mHost = "127.0.0.1";
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
	
	// Logging TAG
	private static String TAG = "DroidSynergy";
	
	//Sleep time for each loop in LooperThread in ms
	private static int LOOPER_DELAY = 20;
	
	// Associated Runnable for the Thread above
	private Runnable mNetworkRunnable = new Runnable() {
		public void run() {
			mConnection.beginConnection();
		}
	};

	private Runnable mLooperRunnable = new Runnable() {
		public void run() {
			while (true) {
				if(!mQueue.isEmpty()){
					Logd("Packet received!");
					mQueue.remove();
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
		// This has to be called before passing the map to parser
		initializesAssociationMap();
		mParser = new SimpleParser(mStringToPacketMap);
		mQueue = new LinkedList<Packet>();
		try {
			mConnection = new StreamConnection(mHost, mPort, mQueue, this, mParser);
			mNetworkThread = new Thread(mNetworkRunnable);
			mNetworkThread.start();
			// Don't run the looper until connection is made
			mLooperThread = new Thread(mLooperRunnable);
		} catch (UnknownHostException e) {
			Logd("UnknownHostException: " + e.getLocalizedMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Logd("IOException!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Logd("onCreate returns");
	}

	// Initializes the association map
	private void initializesAssociationMap() {
		mStringToPacketMap.put(HandshakePacket.mType, new HandshakePacket());
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
		Logd(msg);
	}

	public void problem(String msg) {
		Logd(msg);
	}
	
	// Log.d wrapper
	public void Logd(String msg){
		Log.d(TAG, msg);
	}
	// Log.e then exit
	public void LogFatal(String msg){
		Log.e(TAG, msg);
		System.exit(1);
	}
}
