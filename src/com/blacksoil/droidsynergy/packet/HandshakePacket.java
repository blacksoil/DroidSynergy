package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.response.StringResponse;


// Handshake packet which is the first
// thing a new Synergy client-server connection does
public class HandshakePacket extends Packet {
	private String mType = "Synergy"; 
	private String mDescription = "Synergy Handshake";
	
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
		StringResponse handshakeResponse = new StringResponse("Synergy");
		return handshakeResponse;
	}
	@Override
	public Packet getInstance(List<Byte> packets) {
		return new HandshakePacket();
	}
	
}
