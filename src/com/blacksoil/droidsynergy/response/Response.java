package com.blacksoil.droidsynergy.response;

import java.util.List;

public abstract class Response {
	// Return the Byte list that is ready to be
	// pushed to the server (eg. size is set up correctly)
	public abstract List<Byte> toByteArray();
}
