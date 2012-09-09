package com.blacksoil.droidsynergy.packet;

import java.util.List;

/*
 * This is used by the server to tell the client
 * about the setup of the server.
 * Eg REL events are used instead of ABS events.
 * 
 * At this point we simply do nothing
 */
public class SetOptionPacket extends ResponselessPacket {
	private final static String mType = "DSOP";
	private final static String mDescription = "Set Option";

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
		return new SetOptionPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
