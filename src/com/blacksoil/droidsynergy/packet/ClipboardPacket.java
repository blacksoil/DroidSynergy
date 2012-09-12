package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.NoOpResponse;
import com.blacksoil.droidsynergy.response.Response;

public class ClipboardPacket extends Packet {
	private final static String mType = "DCLP";
	private final static String mDescription = "Clipboard data";
	
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
		return new NoOpResponse();
	}

	@Override
	public Packet getInstance(List<Byte> packets) {
		return new ClipboardPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
