package com.blacksoil.droidsynergy.parser;

import java.util.List;
import java.util.Map;

import android.util.Printer;

import com.blacksoil.droidsynergy.connection.ConnectionCallback;
import com.blacksoil.droidsynergy.packet.Packet;
import com.blacksoil.droidsynergy.utils.Converter;

public class SimpleParser implements Parser {
	// Map that relates the actual string code to a packet
	private Map<String, Packet> mStringToPacketMap;
	
	public SimpleParser(Map<String, Packet> stringToPacketMap){
		if(stringToPacketMap == null)
			throw new IllegalArgumentException("map shouldn't be null!");
		mStringToPacketMap = stringToPacketMap;
	}
	
	public Packet parse(List<Byte> packets) {
		int packetLength;
		
		if(packets == null){
			throw new IllegalArgumentException("Packets shouldn't be null!");
		}
		if(packets.size() < 4){
			throw new IllegalArgumentException("Packets size < 4");
		}
		
		packetLength = Converter.getPacketLength(packets);
		
		if(packetLength <= 0){
			throw new RuntimeException("Invalid packet length: " + packetLength);
		}
		
		// Eat up the packetLength byte
		deleteListUpTo(packets, 4);
		
		if(packetLength != packets.size()){
			throw new RuntimeException("Packet size doesn't match with what claimed: " + packetLength +
					 " and " + packets.size());
		}
		
		return getAppropriatePacket(packets);
		
	}
	
	// Given a packets, return an appropriate packet
	// that associates with the bytes.
	// Return null if no packet applicable 
	private Packet getAppropriatePacket(List<Byte> packets) {
		for(String keyword: mStringToPacketMap.keySet()){
			if(keyword.length() > packets.size())
				continue;
			for(int i=0;i<keyword.length();i++){
				if(keyword.charAt(i) != packets.get(i)){
					continue;
				}
			}
			
			// We found a match!
			// Here is a little tricky.
			// At a compile time, we don't know what packet
			// we're encountering, so we don't know how to 
			// initialize a new appropriate Packet
			Packet packet = mStringToPacketMap.get(keyword);
			// We dupe it indirectly and let it initializes itself
			Packet dupedPacket = packet.getInstance(packets);
			
			return dupedPacket;
		}
		return null;
	}
	
	
	/*
	 * Delete the list's values from the very first beginning
	 * up to "upTo" 
	 */
	private void deleteListUpTo(List<Byte> packets, int upTo){
		for(int i=0;i<upTo;i++){
			packets.remove(0);
		}
	}
}
