package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.utils.Converter;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

/*
 * Mouse wheel movement
 * 
 * This doesn't work yet. (Partially implemented) 
 * Need to translate the Synergy packet that's sent as ABS_WHELL
 * to be REL_WHELL
 */
public class MouseWheelPacket extends ResponselessPacket {
	private final static String mType = "DMWM";
	private final static String mDescription = "Mouse wheel movement";
	
	// Dummy constructor
	public MouseWheelPacket(){
		
	}
	
	// Not really used yet
	public MouseWheelPacket(int x, int y){
		
	}
	
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
		int x,y;
		x = Converter.intFrom16bit(packets.get(1), packets.get(0));
		y = Converter.intFrom16bit(packets.get(3), packets.get(2));
		GlobalLogger.getInstance().getLogger().Logd("Mouse scroll: x=" + x + " y=" + y);
		DroidSynergyShared.getInstance().getInput().mouseWheel(x, y);
		return new MouseWheelPacket(x, y);
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
