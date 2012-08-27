package com.blacksoil.droidsynergy.connection;

import java.util.Queue;

import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.response.Response;

/*
 * Interface that the main connection class
 * would have to implement.
 */
public interface Connection {
	// Return the next packet on the queue
	public Packet getNextPacket();
	// Get the queue object that holds the packet
	public Queue<Packet> getPacketQueue();
	// Write a response packet to the server
	public boolean writeResponse(Response resp);
	
	/*
	 * TODO:
	 * + getConnectionState 
	 */
}
