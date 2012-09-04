package com.blacksoil.droidsynergy.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.parser.Parser;
import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.utils.Converter;
import com.blacksoil.droidsynergy.utils.Utility;

/*
 * Implementation of a Connection
 * This class handles the network connection with
 * Synergy server using a Socket
 * 
 * REMEMBER to run this class in a separate thread!
 * REMEMBER to synchronized on queue!
 */

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

	/*
	 * Arguments:
	 * 
	 * host and port: the server address and port queue: the queue that holds
	 * the parsed packet callback: the callback that notifies the state of the
	 * connection stringToPacketMap: the String to Packet association needed by
	 * Parser class
	 */
	public StreamConnection(String host, int port, Queue<Packet> queue,
			ConnectionCallback callback, Parser parser)
			throws UnknownHostException, IOException {
		mSocket = new Socket(host, port);
		// Get input output stream
		mOut = new DataOutputStream(mSocket.getOutputStream());
		mIn = new DataInputStream(mSocket.getInputStream());
		mPacketQueue = queue;
		mCallback = callback;
		// Initializes the global buffer
		mByteBuffer = new LinkedList<Byte>();
		// Initializes the parser
		mParser = parser;
		// Notify client
		mCallback.connected();
	}

	// The main body of this class
	// Call this to start the main loop
	public void beginConnection() {
		final int BUFFER_SIZE = 512;
		byte[] buffer = new byte[BUFFER_SIZE];
		// Result of read()
		int readlen;
		// Parsed packet length
		int packlen;
		// List to be processed by Parser
		List<Byte> packets;

		while (mSocket.isConnected()) {

			try {
				try {
					// Renew the parser buffer
					packets = new LinkedList<Byte>();

					// Grab the network data!
					readlen = mIn.read(buffer, 0, BUFFER_SIZE);

					// Connection closed by the server
					if (readlen == -1) {
						mCallback.problem("read() == -1");
						mCallback.disconnected();
					}
					else{
						//mCallback.log("Got packet: " + readlen + " bytes.");
					}
					
					// Copy the read() result in to the global buffer
					for (int i = 0; i < readlen; i++) {
						mByteBuffer.add(buffer[i]);
					}

					packlen = Converter.getPacketLength(mByteBuffer);

					// Getting 512 byte in a single read doesn't quite make
					// sense?
					// Something goes wrong?
					if (packlen > 1000) {
						throw new RuntimeException("Read too big: " + packlen);
					}
					
					// We don't have enough data to be processed
					// Loop until we have enough
					if(packlen > mByteBuffer.size()){
						continue;
					}

					// +4 for the packet size itself
					for (int i = 0; i < (packlen + 4); i++) {
						// Move it to the Parser buffer while removing the
						// original
						packets.add(mByteBuffer.remove(0));
					}

					synchronized (mPacketQueue) {
						try{
							Packet parsedPacket = mParser.parse(packets);
							mPacketQueue.add(parsedPacket);
						} catch(RuntimeException e){
							mCallback.error("UnknownPacket: \n" + e.getLocalizedMessage());
						}
					}

				} catch (IOException e) {
					mCallback.problem("read() results in an exception: "
							+ e.getLocalizedMessage());
					e.printStackTrace();
				}

				Thread.sleep(mTimeout);
			} catch (InterruptedException e) {
				mCallback.problem("Thread sleep interrupted: "
						+ e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		mCallback.disconnected();
	}

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
		
		mCallback.log("Sending response: " + responseBytes.size() + " bytes.");
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

}
