package com.blacksoil.droidsynergy.global;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.blacksoil.droidsynergy.input.InputInterface;

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
	// Window Manager
	private WindowManager mWindowManager;
	// Input bridge
	private InputInterface mInput;
	
	public DroidSynergyShared(String clientName, Context ctx, InputInterface input){
		if(clientName == null)
			throw new IllegalArgumentException("clientName can't be null!");
		if(input == null)
			throw new IllegalArgumentException("input shouldn't be null!");
		
		mClientName = clientName;
		mContext = ctx;
		mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		if(mWindowManager == null)
			throw new RuntimeException("Couldn't get WindowManager!");
		mInput = input;
	}
	
	public static DroidSynergyShared getInstance(){
		if (mInstance == null)
			throw new RuntimeException("Initialize DroidSynergyBuild first!");
		return mInstance;
	}
	
	// Called only once to initialized this class data
	public static void initialize(String clientName, Context ctx, InputInterface input){
		if(mInstance != null)
			throw new RuntimeException("initialize() could only be called once!");
		mInstance = new DroidSynergyShared(clientName, ctx, input);
	}
	
	public String getClientName(){
		return mClientName;
	}
	
	public int getScreenWidth(){
		Point size = new Point();
		mWindowManager.getDefaultDisplay().getSize(size);
		return size.x;
	}
	
	public int getScreenHeight(){
		Point size = new Point();
		mWindowManager.getDefaultDisplay().getSize(size);
		return size.y;
	}
	
	// Returns the input handler
	public InputInterface getInput(){
		return mInput;
	}
	
	// These constants are required to respond to HandshakeProtocol
	public static final int SYNERGY_MAJOR_PROTOCOL = 1;
	public static final int SYNERGY_MINOR_PROTOCOL = 4;
}
