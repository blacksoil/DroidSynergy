package com.blacksoil.droidsynergy.parser;

import java.util.List;

import com.blacksoil.droidsynergy.packet.Packet;

/*
 * The class used to parse the byte received
 * from the network. 
 * 
 * NOTE that this class assume correctness in the bytes 
 * received.
 * 
 * Also the bytes for a packet should have complete parts
 * (eg. preffixed by the packet length, and the size of
 * the body is exactly as what the length claims)
 */
public interface ParserInterface {
	// Given the array of packet byte
	// Parse the packet
	// Throw IllegalArgumentException if packets un-parseable
	// Throw RuntimeException if packets size is unacceptable
	// Throw RuntimeException if packets unknown
	public Packet parse(List<Byte> packets);
}
