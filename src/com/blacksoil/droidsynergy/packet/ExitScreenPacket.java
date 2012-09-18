package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.response.ScreenInfoResponse;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

public class ExitScreenPacket extends ResponselessPacket {
	private static final String mType = "COUT";
	private static final String mDescription = "Exit screen";
	private static final boolean DEBUG = true;
	
	public ExitScreenPacket(){
		// Do nothing
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
	public Response generateResponse() {
		if (DEBUG) GlobalLogger.getInstance().getLogger().Logd("Exit Screen");
		return new ScreenInfoResponse();
	}

	@Override
	public Packet getInstance(List<Byte> packets) {
		return new ExitScreenPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}


}
