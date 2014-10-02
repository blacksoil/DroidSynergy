package com.blacksoil.droidsynergy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ServiceStarterActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ServiceStarterActivity";
    private static final int DEFAULT_PORT = 24800;

    private Button mButtonConnect;
    private EditText mTextIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_starter);
        initWidget();
    }

    private void initWidget() {
        mButtonConnect = (Button) findViewById(R.id.btnConnect);
        mTextIp = (EditText) findViewById(R.id.txtIpAddr);
        mButtonConnect.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_starter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        final int btnConnectId = mButtonConnect.getId();
        if (v.getId() == btnConnectId) {
            Log.d(TAG, "Button connect is pressed: " + mTextIp.getText().toString());
            Intent i = new Intent(this, MainService.class);
            i.putExtra("IP_ADDRESS", mTextIp.getText().toString());
            i.putExtra("PORT", DEFAULT_PORT);
            startService(i);
        }
    }

    public void debugLogD(String msg) {
        Log.d(TAG, msg);
    }
}
