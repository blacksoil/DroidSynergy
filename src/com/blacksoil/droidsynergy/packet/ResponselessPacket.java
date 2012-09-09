package com.blacksoil.droidsynergy.packet;

import com.blacksoil.droidsynergy.response.BasicResponse;
import com.blacksoil.droidsynergy.response.Response;

/*
 * Packet that doesn't need to be responded
 */
public abstract class ResponselessPacket extends Packet {

	@Override
	public Response generateResponse() {
		return new BasicResponse();
	}
}
