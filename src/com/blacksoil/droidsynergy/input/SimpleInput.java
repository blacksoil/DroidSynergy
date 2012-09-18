package com.blacksoil.droidsynergy.input;

import java.io.DataOutputStream;
import java.io.IOException;

import com.blacksoil.droidsynergy.utils.GlobalLogger;

// Bridge the JNI that does the actual input messaging
// This class is meant to be initialized only once
public class SimpleInput implements InputInterface{
	private static native int initializeNative();
	private static native void relativeMouseMoveNative(int dx, int dy);
	private final static String UINPUT_PATH = "/dev/uinput";
	
	static {
		System.loadLibrary("input_injector_jni");
	}
	
	// Give uinput the correct permission
	// for input injection
	private void setUinputPermission(){
		// Request a root permission
		Process process;
		try {
			process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("chmod 777 " + UINPUT_PATH);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't gain a root permission!");
		}
		
	}
	
	public SimpleInput(){
		setUinputPermission();
		int code = initializeNative();
		GlobalLogger.getInstance().getLogger().Logd("Initializenative:" + code);
	}
	
	
	public void relativeMouseMove(int dx, int dy) {
		/*
		if(dx > 1 || dx < -1)
			dx /= 2;
		if(dy > 1 || dy < -1)
			dy /= 2;
		*/
		relativeMouseMoveNative(dx,dy);
		
	}

	public void leftMouseDown() {
		// TODO Auto-generated method stub
		
	}

	public void leftMouseUp() {
		// TODO Auto-generated method stub
		
	}

}
