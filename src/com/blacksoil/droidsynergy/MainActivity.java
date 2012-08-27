package com.blacksoil.droidsynergy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.blacksoil.droidsynergy.packet.HandshakePacket;

public class MainActivity extends Activity {
	String mHost = "192.168.1.8";
	int mPort = 24800;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
