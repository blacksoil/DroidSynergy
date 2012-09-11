package com.blacksoil.droidsynergy.response;

import java.util.LinkedList;
import java.util.List;

import com.blacksoil.droidsynergy.utils.Converter;

public abstract class Response {
	// The actual byte representation the response contains
	protected List<Byte> mByte;
	
	// Constructor that only initializes mByte
	protected Response(){
		// Since we append new bytes into it,
		// LinkedList is the best choice
		mByte = new LinkedList<Byte>();
	}
	
	// Return the Byte list that is ready to be
	// pushed to the server (eg. size is set up correctly)
	//
	// TODO: Might not need to dupe to increase efficiency?
	public List<Byte> toByteArray(){
		// Return a copy
		List<Byte> toReturn = new LinkedList<Byte>();
		toReturn.addAll(mByte);
		
		// If the response is empty, don't
		// append the size because this is
		// a response-less packet
		if(toReturn.size() == 0){
			return toReturn;
		}
		// Add the packet size
		Converter.insertSize(toReturn);
		return toReturn;
	}
	
}
