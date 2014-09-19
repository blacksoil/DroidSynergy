package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

/*
 * Packet that indicates a mouse button is held down
 * DMDN%i
 * Where the integer indicates which button is clicked
 */
public class MouseDownPacket extends MousePacket {
	private final static String mType = "DMDN";
	private final static String mDescription = "Mouse button down";
	
	// Dummy constructor
	public MouseDownPacket(){
	}
	
	// button_type is the button being clicked
	public MouseDownPacket(int button_type){
		
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
		GlobalLogger.getInstance().getLogger().Logd("Mouse down");
		
		//
		if(packets.size() < 1)
			throw new RuntimeException("packets for mouse click needs to be larger");
		int mouse_type = packets.get(0);
		interpretButton(mouse_type);
		return new MouseDownPacket(mouse_type);
	}

	@Override
	public String toString() {
		return mDescription;
	}

	@Override
	protected void leftButton() {
		DroidSynergyShared.getInstance().getInput().leftMouseDown();
	}

	@Override
	protected void rightButton() {
		// TODO Auto-generated method stub
		
	}

}
