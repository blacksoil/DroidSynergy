package com.blacksoil.droidsynergy.response;

import com.blacksoil.droidsynergy.constant.DroidSynergyBuild;

/*
 * This is the response of HandshakePacket
 * The packet description is described at HandshakePacket class
 */
public class HandshakeResponse extends BasicResponse {
	public HandshakeResponse(String client_name){
		this(client_name, 
				DroidSynergyBuild.SYNERGY_MAJOR_PROTOCOL,
				DroidSynergyBuild.SYNERGY_MINOR_PROTOCOL);
	}
	
	public HandshakeResponse(String client_name,
			int protocol_major, int protocol_minor){
		super();
		appendString("Synergy");
		appendInt16(protocol_major);
		appendInt16(protocol_minor);
		appendInt32(client_name.length());
		appendString(client_name);
	}
}
