package com.blacksoil.droidsynergy;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.blacksoil.droidsynergy.global.DroidSynergyShared;
import com.blacksoil.droidsynergy.input.SimpleInput;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

import Hooker.RunnerThread;

public class MainService extends Service implements Logger {
    private static final String TAG = "MainService";

    public void Logd(String msg) {
        Log.d(TAG, msg);
    }

    public void Loge(String msg) {
        Log.e(TAG, msg);
    }

    public void Logi(String msg) {
        Log.i(TAG, msg);
    }

    private class MainRunner implements Runnable {
        private String mIpAddr;
        private int mCount = 0;
        private boolean mShouldRun;

        public MainRunner(String ipAddress) {
            debugLogD("MainRunner is initialized with ipAddr: " + ipAddress);
            mIpAddr = ipAddress;
            mShouldRun = true;
        }
        public void shouldNotRun() {
            debugLogD("Interrupted! Setting mShouldRun=false!");
            mShouldRun = false;
        }

        public void run() {
            while (mShouldRun) {
                debugLogD("Running for " + mCount++ + " times.");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    private RunnerThread mRunnerThread;
    private Handler mHandler;

    public MainService() {
        debugLogD("MainService constructor is called!");
        mHandler = new Handler();
        new GlobalLogger(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        final String ipAddr = intent.getStringExtra("IP_ADDRESS");
        final int port = intent.getIntExtra("PORT", 0);
        Point screenSize = new Point();
        screenSize.set(intent.getIntExtra("SCREEN_WIDTH", 0), intent.getIntExtra("SCREEN_HEIGHT", 0));

        if (screenSize.x == 0 || screenSize.y == 0) {
            logE("Invalid screen size is passed by ServiceStarterActivity!");
        }

        if (port == 0) {
            logE("Invalid port! Did it passed by the starting activity?");
        }

        debugLogD("onStartCommand: flags=" + flags + " startId=" + startId);
        debugLogD("Ip Addr: " + ipAddr + ". Port:" + port);

        // Initializes constants required
        DroidSynergyShared.initialize("android", this, new SimpleInput(), screenSize);

        Runnable runThread = new Runnable() {
            public void run() {
                mRunnerThread = new RunnerThread(ipAddr, port);
                mRunnerThread.start();
            }
        };

        if (mRunnerThread != null) {
            debugLogD("RunnerThread is already running, stopping it!");
            mRunnerThread.interrupt();
            mHandler.postDelayed(runThread, 5000);
        }
        else {
            mHandler.post(runThread);
        }


        return START_NOT_STICKY;
    }

    private void logE(String msg) {
        Log.e(TAG,msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        debugLogD("Binding isn't supported in MainService");
        return null;
    }

    private void debugLogD(String msg) {
        Log.d(TAG, msg);
    }
}
