package com.blacksoil.droidsynergy.packet;

import java.util.List;

public class InfoAcknowledgmentPacket extends ResponselessPacket {
	/*
	 * A request for command info acknowledgment?
	 * No need to reply with anything
	 */
	private final static String mType = "CIAK";
	private final static String mDescription = "Command Info acknowledgment";

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
		return new InfoAcknowledgmentPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}