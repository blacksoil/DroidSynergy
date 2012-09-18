package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.response.HandshakeResponse;
import com.blacksoil.droidsynergy.response.Response;

/*
 * Handshake packet looks like this: "7Synergy"
 * With 7 the packet length prefixing the packet body.
 * 
 * 
 * The reply is as follow:
 * Synergy%2i%2i%s
 * 
 * With:
 * %2i = 32-bit int
 * %s = String
 * 
 * The first %2i is really two 16-bit integers
 * representing the Synergy Major and Minor
 * (Big-endian byte ordering)
 * 
 * The second %2i is the length of %s
 * 
 * %s is the client name that the server is expecting
 * 
 */
public class HandshakePacket extends Packet {
	private final static String mType = "Synergy";
	private final static String mDescription = "Synergy Handshake";

	public class Argument {
		public String client_name;
	}

	@Override
	public String getType() {
		return mType;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

	@Override
	public Response generateResponse() {
		// Simply reply with a packet that says "Synergy" to reply the handshake
		Response handshakeResponse = new HandshakeResponse(DroidSynergyShared.getInstance().getClientName());
		return handshakeResponse;
	}

	@Override
	public Packet getInstance(List<Byte> packets) {
		return new HandshakePacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
