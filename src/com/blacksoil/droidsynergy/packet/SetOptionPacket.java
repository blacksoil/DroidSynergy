package com.blacksoil.droidsynergy.packet;

import java.util.List;

import com.blacksoil.droidsynergy.response.NoOpResponse;
import com.blacksoil.droidsynergy.response.Response;

/*
 * This is used by the server to tell the client
 * about the setup of the server.
 * Eg REL events are used instead of ABS events.
 * 
 * At this point we simply do nothing.
 * But for some reasons if we don't reply the server doesn't like it.
 * To trick it we response with CNOP
 */
public class SetOptionPacket extends Packet {
	private final static String mType = "DSOP";
	private final static String mDescription = "Set Option";

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
		return new SetOptionPacket();
	}

	@Override
	public String toString() {
		return mDescription;
	}

	@Override
	public Response generateResponse() {
		return new NoOpResponse();
	}

}
