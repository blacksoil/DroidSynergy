package com.blacksoil.droidsynergy.utils;

import java.util.List;

import com.blacksoil.droidsynergy.response.Response;

public class Utility {
	// Given a response
	// Convert the byte on the response to a string
	public static String dump(Response response){
		String dump_result = "";
		List<Byte> bytes = response.toByteArray();
		dump_result = dump(bytes);
		return dump_result;
	}
	
	// Given a list of Byte
	// return the String representation of it
	public static String dump(List<Byte> data){
		String dump_result = "";
		if(data== null)
			return "null";
		for(Byte b: data){
			dump_result += ((byte) b.byteValue()) + " (" + (char)(b.byteValue()) + ")\n";
		}
		return dump_result;
	}
}
