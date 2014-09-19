package com.blacksoil.droidsynergy.utils;

import java.util.LinkedList;
import java.util.List;

// This class does conversion 
// necessary for a packet to be transmitted throughout network
// Big-endian ordering is used for integer
public class Converter {

	// Add the given string to a list of byte
	public static void appendString(List<Byte> dest, String src) {
		if (dest == null || src == null) {
			throw new IllegalArgumentException("dest/src can't be null!");
		}
		for (int i = 0; i < src.length(); i++) {
			dest.add((byte) src.charAt(i));
		}
	}

	// Given a list of byte that doesn't its data length
	// this methods add the length, which is
	// the number of byte in the list inserted to the begnning
	// as a 4-byte values in a big-endian ordering
	public static void insertSize(List<Byte> dest) {
		int size = dest.size();
		byte[] size_converted = convertInt32ToNetwork(size);
		for (int i = (size_converted.length - 1); i >= 0; i--) {
			dest.add(0, size_converted[i]);
		}
	}

	// Append 16-bit integer to the end of the list
	public static void appendInt16(List<Byte> dest, int src) {
		if (dest == null)
			throw new IllegalArgumentException("dest can't be null");
		byte[] src_converted = convertInt16ToNetwork(src);
		
		for (int i = 0; i < src_converted.length; i++) {
			dest.add(src_converted[i]);
		}
	}

	// Append 32-bit integer to the end of the list
	public static void appendInt32(List<Byte> dest, int src) {
		if (dest == null)
			throw new IllegalArgumentException("dest can't be null");
		byte[] src_converted = convertInt32ToNetwork(src);
		for (int i = 0; i < src_converted.length; i++) {
			dest.add(src_converted[i]);
		}
	}

	public static byte[] convertInt16ToNetwork(int src) {
		byte[] result = new byte[2];
		result[0] = (byte) (src >> 8);
		result[1] = (byte) src;
		return result;
	}

	public static byte[] convertInt32ToNetwork(int src) {
		byte[] result = new byte[4];
		result[0] = (byte) (src >> 24);
		result[1] = (byte) (src >> 16);
		result[2] = (byte) (src >> 8);
		result[3] = (byte) src;
		return result;
	}

	// Parse the length of the received Synergy packet
	// The length is big-endian formatted as it is
	// using network representation
	public static int getPacketLength(List<Byte> packet) {
		if (packet == null)
			throw new IllegalArgumentException("packet can't be null!");

		if (packet.size() < 4)
			throw new IllegalArgumentException("packet size less than 4");
		int packet_size = 0;
		packet_size |= ((packet.get(0) & 0xFF) << 24);
		packet_size |= ((packet.get(1) & 0xFF) << 16);
		packet_size |= ((packet.get(2) & 0xFF) << 8);
		packet_size |= (packet.get(3) & 0xFF);
		return packet_size;
	}

	public static int getPacketLength(byte[] packet) {
		if (packet.length < 4)
			throw new IllegalArgumentException("packet size less than 4");

		List<Byte> packet_list = new LinkedList<Byte>();
		for (int i = 0; i < packet.length; i++) {
			packet_list.add(packet[i]);
		}
		return getPacketLength(packet_list);
	}
	
	// Convert bytes into integer
	public static int intFrom16bit(byte MSB, byte LSB){
		int result = 0;
		
		// We don't mask because we actually
		// want the sign to be correct
		// Java does the sign extension by default
		result |= (MSB);
		result <<= 8;
		// We mask because we don't use the sign
		// of LSB
		result |= (LSB & 0xFF);
		return result;
		
		
		/*
		result |= MSB << 8;
		result |= (byte)LSB & 0xFF;
		
		return result;
		*/
		/*
		int a = MSB << 8;
		int b = LSB;
		if(a < 0){
			throw new RuntimeException("a negative!:" + a);
		}
		if(b < 0){
			throw new RuntimeException("b negative!:" + b);
		}
		result = a +b;
		return result;
		*/
		
	}
}
