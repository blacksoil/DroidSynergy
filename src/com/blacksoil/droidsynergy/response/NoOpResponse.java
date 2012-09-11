package com.blacksoil.droidsynergy.response;

public class NoOpResponse extends BasicResponse {
	public NoOpResponse(){
		super();
		appendString("CNOP");
	}

}
