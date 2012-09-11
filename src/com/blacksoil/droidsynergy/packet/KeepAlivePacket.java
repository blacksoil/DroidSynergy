package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.KeepAliveResponse;
import com.blacksoil.droidsynergy.response.Response;

/*
 * Keep Alive packet
 * Essentially the server requests this to be sure
 * that the connection is still maintained.
 * 
 * Simply reply with the string CALV
 */
public class KeepAlivePacket extends Packet {
	private final static String mType = "CALV";
	private final static String mDescription = "Keep Alive";

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
		return new KeepAliveResponse();
	}

	@Override
	public Packet getInstance(List<Byte> packets) {
		return new KeepAlivePacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
