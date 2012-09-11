package com.blacksoil.droidsynergy.packet;

import java.util.List;

/*
 * This is called when the mouse just entered
 * the target's screen.
 * For now on we don't do anything because we handle
 * the injection on the Main Activity class.
 */
public class EnterScreenPacket extends ResponselessPacket {
	private final static String mType = "CINN";
	private final static String mDescription = "Mouse enter the target screen.";

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
		return new EnterScreenPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
