package com.blacksoil.droidsynergy.global;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.blacksoil.droidsynergy.input.InputInterface;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

/*
 * This is a class
 * consisting of Build-related constants and shared
 * data.
 * (eg. Synergy Major and Minor)
 */
public class DroidSynergyShared {
	// A singleton instance
	private static DroidSynergyShared mInstance = null;
	// Client name
	private String mClientName;
	// Main activity's context
	private Context mContext; 
	// Input bridge
	private InputInterface mInput;

    private Point mScreenSize;

    private final static boolean DEBUG = true;
	
	public DroidSynergyShared(String clientName, Context ctx, InputInterface input, Point screenSize){
		if(clientName == null)
			throw new IllegalArgumentException("clientName can't be null!");
		if(input == null)
			throw new IllegalArgumentException("input shouldn't be null!");
		
		mClientName = clientName;
		mContext = ctx;
		mInput = input;
        mScreenSize = screenSize;
	}
	
	public static DroidSynergyShared getInstance(){
		if (mInstance == null)
			throw new RuntimeException("Initialize DroidSynergyBuild first!");
		return mInstance;
	}
	
	// Called only once to initialized this class data
	public static void initialize(String clientName, Context ctx, InputInterface input, Point screenSize){
		if(mInstance != null)
			throw new RuntimeException("initialize() could only be called once!");
		mInstance = new DroidSynergyShared(clientName, ctx, input, screenSize);
	}
	
	public String getClientName(){
		return mClientName;
	}
	
	public int getScreenWidth(){
        return mScreenSize.x;
	}
	
	public int getScreenHeight(){
        return mScreenSize.y;
	}
	
	// Returns the input handler
	public InputInterface getInput(){
		return mInput;
	}

    public void LogD(String msg) {
        if (DEBUG) {
            GlobalLogger.getInstance().getLogger().Logd(msg);
        }
    }
	
	// These constants are required to respond to HandshakeProtocol
	public static final int SYNERGY_MAJOR_PROTOCOL = 1;
	public static final int SYNERGY_MINOR_PROTOCOL = 4;
}
