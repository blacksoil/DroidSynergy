package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.NoOpResponse;
import com.blacksoil.droidsynergy.response.Response;

public class CClipboardPacket extends Packet {
	private final static String mType = "CCLP";
	private final static String mDescription = "CClipboard data";
	
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
		return new CClipboardPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
