package com.blacksoil.droidsynergy.packet;

import com.blacksoil.droidsynergy.utils.GlobalLogger;

public abstract class MousePacket extends ResponselessPacket {

	protected final int LEFT_BUTTON = 1;
	protected final int UNKNOWN_BUTTON = -1;

	protected void interpretButton(int mouseByte){
		if(mouseByte == LEFT_BUTTON){
			leftButton();
		}
		else{
			unknownButton(mouseByte);
		}
	}
	
	// These methods will be invoked depending on which 
	// mouse button is clicked
	protected abstract void leftButton();
	protected abstract void rightButton();
	
	// Called when an unrecognized mouse button 
	public void unknownButton(int mouse_type){
		GlobalLogger.getInstance().getLogger().Logd("Unknown mouse type: " + mouse_type);
	}

}
