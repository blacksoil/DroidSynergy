package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.utils.GlobalLogger;

/*
 * This is called when the mouse just entered
 * the target's screen.
 * For now on we don't do anything because we handle
 * the injection on the Main Activity class.
 */
public class EnterScreenPacket extends ResponselessPacket {
	private final static String mType = "CINN";
	private final static String mDescription = "Mouse enter the target screen.";
	private final static boolean DEBUG = true;
	
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
		if (DEBUG) GlobalLogger.getInstance().getLogger().Logd("Enter Screen");
		return new EnterScreenPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
