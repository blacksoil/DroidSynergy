package com.blacksoil.droidsynergy.packet;

import java.util.List;

public class ResetOptionPacket extends ResponselessPacket {
	/*
	 * A request for command reset option?
	 * No need to reply with anything
	 */
	private final static String mType = "CROP";
	private final static String mDescription = "Command reset option";

	@Override
	public String getType() {
		return mType;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}
	
	@Override
	public Packet getInstance(List<Byte> packets) {
		return new ResetOptionPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}