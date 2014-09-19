package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.Response;

public abstract class Packet {

	// Packet type
	public abstract String getType();

	// Packet type description
	public abstract String getDescription();

	// Do the appropriate action depending on the packet type
	// Return true if handling success
	public abstract Response generateResponse();
	
	// Get a new packet Object that initializes according
	// to the given packets
	public abstract Packet getInstance(List<Byte> packets);
	
	// String representation of the packet
	// For debugging purposes
	public abstract String toString();
}
