package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.utils.Converter;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

/*
 * Packet that request mouse movement
 * Relative movement
 */
public class RelativeMovePacket extends ResponselessPacket {
	private final static String mType = "DMRM";
	private final static String mDescription = "Mouse move rel";
	private final static boolean DEBUG = false;

	// Dummy constructor
	public RelativeMovePacket() {
	}

	public RelativeMovePacket(int x, int y) {

	}

	@Override
	public String getType() {
		return mType;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

	// Up to this point the packet's size has already been stripped
	@Override
	public Packet getInstance(List<Byte> packets) {
		if (packets.size() < 4) {
			throw new RuntimeException("Inappropriate network packet size "
					+ "for MouseMovePacket");
		}
		
		// GlobalLogger.getInstance().getLogger().Logd(Utility.dump(packets));
		int x = Converter.intFrom16bit(packets.get(0), packets.get(1));
		int y = Converter.intFrom16bit(packets.get(2), packets.get(3));
		if (DEBUG)
			GlobalLogger.getInstance().getLogger().Logd("X:" + x + " Y:" + y);
		DroidSynergyShared.getInstance().getInput().relativeMouseMove(x, y);
		return new RelativeMovePacket(x, y);
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
