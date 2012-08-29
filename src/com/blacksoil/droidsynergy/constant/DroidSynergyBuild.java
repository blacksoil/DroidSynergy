package com.blacksoil.droidsynergy.constant;

/*
 * This is a static class
 * consisting of Build-related constants
 * (eg. Synergy Major and Minor)
 */
public class DroidSynergyBuild {
	// A singleton instance
	private static DroidSynergyBuild mInstance = null;
	// Client name
	private String mClientName;
	
	public DroidSynergyBuild(String clientName){
		if(clientName == null)
			throw new IllegalArgumentException("clientName can't be null!");
		mClientName = clientName;
	}
	
	public static DroidSynergyBuild getInstance(){
		if (mInstance == null)
			throw new RuntimeException("Initialize DroidSynergyBuild first!");
		return mInstance;
	}
	
	public static void initialize(String clientName){
		if(mInstance != null)
			throw new RuntimeException("initialize() could only be called once!");
		mInstance = new DroidSynergyBuild(clientName);
	}
	
	public String getClientName(){
		return mClientName;
	}
	
	// These constants are required to respond to HandshakeProtocol
	public static final int SYNERGY_MAJOR_PROTOCOL = 1;
	public static final int SYNERGY_MINOR_PROTOCOL = 4;
}
