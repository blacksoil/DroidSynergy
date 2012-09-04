package com.blacksoil.droidsynergy.utils;

import com.blacksoil.droidsynergy.Logger;

// A convenient class to do logging anywhere
public class GlobalLogger {
	private static GlobalLogger mInstance = null;
	private Logger mLogger;
	
	public GlobalLogger(Logger logger){
		mInstance = this;
		mLogger = logger;
	}
	
	public static GlobalLogger getInstance() {
		if(mInstance == null)
			throw new RuntimeException("GlobalLogger needs to be initialized first!");
		return mInstance;
	}
	
	public Logger getLogger(){
		return mLogger;
	}
	
}
