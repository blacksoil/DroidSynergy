package com.blacksoil.droidsynergy.input;

public interface InputInterface {
	// Displace the mouse cursor by dx and dy 
	void relativeMouseMove(int dx,int dy);
	
	//
	void leftMouseDown();
	//
	void leftMouseUp();
	//
	void rightMouseDown();
	//
	void rightMouseUp();
	//
	void mouseWheel(int x,int y);
	
}
