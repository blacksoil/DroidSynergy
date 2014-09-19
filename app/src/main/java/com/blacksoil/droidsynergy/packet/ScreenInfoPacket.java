package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.Response;
import com.blacksoil.droidsynergy.response.ScreenInfoResponse;

/*
 * A request for screen info
 * The request looks like: QINF
 * The reply:
 * DINF%2i%2i%2i%2i%2i%2i%2i
 * %2i(s) respectively are:
 * x -> mouse x coord
 * y -> mouse y coord
 * clientWidth  -> screen width 
 * clientHeight -> screen height
 * warp -> 0?
 * mx -> 0?
 * my -> 0?
 */
public class ScreenInfoPacket extends Packet {
	private static final String mType = "QINF";
	private static final String mDescription = "Screen info";
	
	public ScreenInfoPacket(){
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
		return new ScreenInfoResponse();
	}

	@Override
	public Packet getInstance(List<Byte> packets) {
		return new ScreenInfoPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

}
