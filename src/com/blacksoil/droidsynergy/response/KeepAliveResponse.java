package com.blacksoil.droidsynergy.response;

public class KeepAliveResponse extends BasicResponse {
	public KeepAliveResponse(){
		super();
		appendString("CALV");
	}
}
