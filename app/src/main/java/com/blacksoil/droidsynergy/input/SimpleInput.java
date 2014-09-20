package com.blacksoil.droidsynergy.input;

import java.io.DataOutputStream;
import java.io.IOException;

import com.blacksoil.droidsynergy.utils.GlobalLogger;

// Bridge the JNI that does the actual input messaging
// This class is meant to be initialized only once
public class SimpleInput implements InputInterface{
	
	
	private final static String UINPUT_PATH = "/dev/uinput";
	
	private static native int initializeNative();
	// Relative mouse movement
	private static native void relativeMouseMoveNative(int dx, int dy);
	// Mouse click
	private static native void leftMouseDownNative();
	private static native void leftMouseUpNative();
	private static native void rightMouseDownNative();
	private static native void rightMouseUpNative();
    private static native void mouseWheelNative(int x,int y);
    private static native void mouseAbsNative(int x,int y);
	
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
		// Appropriate permission is needed for injection
		setUinputPermission();
		// Initializes the JNI library
		int code = initializeNative();
		GlobalLogger.getInstance().getLogger().Logd("Initialize native:" + code);
		// TODO: Be able to obtain error message from the JNI layer
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

	public void leftMouseDown(){
		leftMouseDownNative();
	}
		

	public void leftMouseUp() {
		leftMouseUpNative();
		
	}
	
	public void rightMouseDown() {
		rightMouseDownNative();
		
	}

    public void mouseAbs(int x, int y) {
        mouseAbsNative(x, y);
    }
	
	public void rightMouseUp() {
		rightMouseUpNative();		
	}

	public void mouseWheel(int x, int y) {
		mouseWheelNative(x, y);
	}

}
