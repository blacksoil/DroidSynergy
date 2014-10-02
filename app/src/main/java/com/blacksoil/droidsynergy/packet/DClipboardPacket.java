package com.blacksoil.droidsynergy.packet;

import com.blacksoil.droidsynergy.response.NoOpResponse;
import com.blacksoil.droidsynergy.response.Response;

import java.util.List;

public class DClipboardPacket extends Packet {
	private final static String mType = "DCLP";
	private final static String mDescription = "DClipboard data";
	
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
		return new DClipboardPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
