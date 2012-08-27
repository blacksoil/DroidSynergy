package com.blacksoil.droidsynergy.response;

import java.util.LinkedList;
import java.util.List;

import com.blacksoil.droidsynergy.utils.Converter;

public class StringResponse extends Response {
	String mResponse;
	
	public StringResponse(String response){
		if(response == null){
			throw new IllegalArgumentException("Response can't be null");
		}
		mResponse = response;
	}
	
	@Override
	public List<Byte> toByteArray() {
		List<Byte> out = new LinkedList<Byte>();
		Converter.appendString(out, mResponse);
		Converter.insertSize(out);
		return out;
	}

}
