package com.blacksoil.droidsynergy.constant;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

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
	// Main activity's context
	private Context mContext; 
	// Window Manager
	private WindowManager mWindowManager;
	
	private int mScreenWidth, mScreenHeight;
	
	public DroidSynergyBuild(String clientName, Context ctx){
		if(clientName == null)
			throw new IllegalArgumentException("clientName can't be null!");
		mClientName = clientName;
		mContext = ctx;
		mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		if(mWindowManager == null)
			throw new RuntimeException("Couldn't get WindowManager!");
	}
	
	public static DroidSynergyBuild getInstance(){
		if (mInstance == null)
			throw new RuntimeException("Initialize DroidSynergyBuild first!");
		return mInstance;
	}
	
	public static void initialize(String clientName, Context ctx){
		if(mInstance != null)
			throw new RuntimeException("initialize() could only be called once!");
		mInstance = new DroidSynergyBuild(clientName, ctx);
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
	
	// These constants are required to respond to HandshakeProtocol
	public static final int SYNERGY_MAJOR_PROTOCOL = 1;
	public static final int SYNERGY_MINOR_PROTOCOL = 4;
}
