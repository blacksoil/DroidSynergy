package com.blacksoil.droidsynergy.utils;

import java.util.List;

import android.util.Printer;

import com.blacksoil.droidsynergy.response.Response;

public class Utility {
	public static void dump(Response response, Printer printer){
		List<Byte> bytes = response.toByteArray();
		if(bytes == null)
			throw new IllegalArgumentException("response returns null!");
		for(Byte b:bytes){
			printer.println("" + (byte) b.byteValue());
		}
	}
}
