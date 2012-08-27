package com.blacksoil.droidsynergy.connection;

/*
 * Callback requires by the implementor of Connection class
 * This class notifies the connection state
 */
public interface ConnectionCallback {
	public void connected();
	public void disconnected();
	public void error(String msg);
	// When a recoverable problem occurred (eg. exception thrown)
	public void problem(String msg);
}
