package com.blacksoil.droidsynergy;

public interface Logger {
	// Wrapper to Log.d
	public void Logd(String msg);
	// Wrapper to Log.e and then exit
	public void Loge(String msg);
	// Wrapper to Log.i
	public void Logi(String msg);
}
