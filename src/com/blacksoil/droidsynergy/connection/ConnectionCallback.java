package com.blacksoil.droidsynergy.connection;

/*
 * Callback requires by the implementor of Connection class
 * This class notifies the connection state
 */
public interface ConnectionCallback {
	// Called when the connection is established
	public void connected();
	// Called when the connection gets disconnected appropriately
	public void disconnected();
	// When this gets called, the connection is also closed
	public void error(String msg);
	// When a recoverable problem occurred (eg. exception thrown)
	public void problem(String msg);
	// Logging related to connection
	public void log(String msg);
}
