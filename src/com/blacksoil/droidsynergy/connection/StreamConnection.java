package com.blacksoil.droidsynergy.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.parser.Parser;
import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.utils.Converter;
import com.blacksoil.droidsynergy.utils.Utility;

public class StreamConnection implements Connection {
	// Input output streams
	private DataOutputStream mOut;
	private DataInputStream mIn;
	// Socket connection
	private Socket mSocket;
	// Callback for the connection
	private ConnectionCallback mCallback;
	// Queue that holds the parsed packet
	private Queue<Packet> mPacketQueue;
	// Timeout between each read() call in ms
	private int mTimeout;
	// The global buffer to hold the packet byte
	private List<Byte> mByteBuffer;
	// Parser that is being used
	private Parser mParser;
	// Indicates whether the socket is connected or not
	private boolean mConnected = false;
	// A single read() buffer size
	private static final int BUFFER_SIZE = 1024;
	private static boolean DEBUG = false;
	/*
	 * Arguments:
	 * 
	 * host and port: the server address and port queue: the queue that holds
	 * the parsed packet callback: the callback that notifies the state of the
	 * connection stringToPacketMap: the String to Packet association needed by
	 * Parser class
	 */
	public StreamConnection(String host, int port, Queue<Packet> queue,
			ConnectionCallback callback, Parser parser) {
		
		// Delay for the main loop
		mTimeout = 50;
		mPacketQueue = queue;
		mCallback = callback;
		
		try{
			mSocket = new Socket(host, port);
			// Set TCP no delay
			mSocket.setTcpNoDelay(true);
			// Get input output stream
			mOut = new DataOutputStream(mSocket.getOutputStream());
			mIn = new DataInputStream(mSocket.getInputStream());
			// Initializes the global buffer
			mByteBuffer = new LinkedList<Byte>();
			// Initializes the parser
			mParser = parser;	
			// Notify client
			mCallback.connected();
			mConnected = true;
		} 
		catch(Exception ex){
			mCallback.error(ex.getLocalizedMessage());
		}
		
	}

	public boolean isConnected(){
		return mConnected && mSocket.isConnected();
	}
	
	// The main body of this class
	// Call this to start the main loop
	public void beginConnection() {
		byte[] buffer = new byte[BUFFER_SIZE];
		// Result of read()
		int readlen;
		
		// Kicks in the thread whose job
		// is to parse the received bytes
		new Thread(mReceivedDataParser).start();
		
		while (isConnected()) {
			try {
				try {
					// Grab the network data!
					readlen = mIn.read(buffer, 0, BUFFER_SIZE);
					if(DEBUG) mCallback.log("read() just returned!");
					// Connection closed by the server
					if (readlen < 0) {
						mCallback.problem("read() : " + readlen);
						// Thread.sleep(2000);
						mConnected = false;
						mCallback.disconnected();
					}
					else{
						if(DEBUG) mCallback.log("Got packet: " + readlen + " bytes.");
					}
					
					synchronized(mByteBuffer){
						// Copy the read() result in to the global buffer
						for (int i = 0; i < readlen; i++) {
							mByteBuffer.add(buffer[i]);
						}
						mByteBuffer.notifyAll();
					}
					
					
				} catch (IOException e) {
					mCallback.problem("read() results in an exception: "
							+ e.getLocalizedMessage());
					e.printStackTrace();
				}
				
				// Sleep a while
				Thread.sleep(mTimeout);
			} catch (InterruptedException e) {
				mCallback.problem("Thread sleep interrupted: "
						+ e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		mCallback.disconnected();
	}
	
	public Runnable mReceivedDataParser = new Runnable(){
		public void run(){
			while(isConnected()){
				int packlen;
				List<Byte> packets = new LinkedList<Byte>();
				
				// We don't have enough data be to interpreted
				// We wait until we do
				synchronized(mByteBuffer){
					while(mByteBuffer.size() < 4){
						try {
							mByteBuffer.wait();
						} catch (InterruptedException e) {
							mCallback.log("wait() is interrupted!");
							e.printStackTrace();
							break;
						}
						//mCallback.log("Skipping because data < 4 bytes");
					}
				}
				
				synchronized(mByteBuffer){
					packlen = Converter.getPacketLength(mByteBuffer);
					if(DEBUG) mCallback.log("Packet length: " + packlen);
					//The interpreted packet length is invalid
					if(packlen <= 0){
						mCallback.log("Unusual packet length: " + packlen);
						mCallback.error(Utility.dump(mByteBuffer));
						//mCallback.log("Actual global buffer size: " + mByteBuffer.size());
						
					}
				}
				
				
				
				// Having a packet with length > 512 doesn't quite make sense?
				// Something goes wrong?
				if (packlen > BUFFER_SIZE) {
					mCallback.error("read() returns > 512 : " + packlen);
				}
				
				// We don't have enough data to be processed
				// Loop until we have enough
				synchronized(mByteBuffer){
					while(packlen > mByteBuffer.size()){
						try {
							mByteBuffer.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//mCallback.log("Not enough data to be processed. " +
						//				"Waiting for the next read() cycle");
					}

					// +4 for the packet size itself
					for (int i = 0; i < (packlen + 4); i++) {
						// Move it to the Parser buffer while removing the
						// original
						packets.add(mByteBuffer.remove(0));
					}
					try{
						Packet parsedPacket = mParser.parse(packets);
						mPacketQueue.add(parsedPacket);
					} catch(RuntimeException e){
						mCallback.error("UnknownPacket: \n" + e.getLocalizedMessage());
					}
				}


			}
		}
	};
	
	public Packet getNextPacket() {
		synchronized (mPacketQueue) {
			if (!mPacketQueue.isEmpty()) {
				return mPacketQueue.remove();
			}
			return null;
		}
	}

	public Queue<Packet> getPacketQueue() {
		return mPacketQueue;
	}

	public boolean writeResponse(Response resp) {		
		List<Byte> responseBytes = resp.toByteArray();
		if (responseBytes == null) {
			throw new IllegalArgumentException("resp shouldn't be null!");
		}
		
		if(DEBUG) mCallback.log("Sending response: " + responseBytes.size() + " bytes.");
		//mCallback.log(Utility.dump(responseBytes));
		// No need to flush or send
		if(responseBytes.size() == 0){
			return true;
		}
		
		//mCallback.log("Queue left: " + mPacketQueue.size());
		//mCallback.log(Utility.dump(responseBytes));
		try {
			for(int i = 0 ; i < responseBytes.size() ; i++){
				mOut.write(responseBytes.get(i));
				//mCallback.log(""+responseBytes.get(i));
			}			
			mOut.flush();
		} catch (IOException e) {
			mCallback.problem("write() fails");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public int getQueueSize(){
		return mPacketQueue.size();
	}

}
