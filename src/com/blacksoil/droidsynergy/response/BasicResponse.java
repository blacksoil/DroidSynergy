package com.blacksoil.droidsynergy.response;

import com.blacksoil.droidsynergy.utils.Converter;

/*
 * Basic response building block
 * This is a response where String or different kind of integer
 * can be appended into.
 * 
 */
public class BasicResponse extends Response {
	public BasicResponse(){
		// Let the superclass initializes mByte for us
		super();
	}
	
	public void appendString(String src){
		if(src == null){
			throw new IllegalArgumentException("str can't be null!");
		}
		Converter.appendString(mByte, src);
	}
	
	public void appendInt16(int src){
		Converter.appendInt16(mByte, src);
	}
	
	public void appendInt32(int src){
		Converter.appendInt32(mByte, src);
	}
	

}
