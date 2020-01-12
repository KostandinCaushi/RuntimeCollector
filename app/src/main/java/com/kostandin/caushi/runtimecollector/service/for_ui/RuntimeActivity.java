package com.kostandin.caushi.runtimecollector.service.for_ui;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.kostandin.caushi.runtimecollector.service.RuntimeService;

public class RuntimeActivity extends AppCompatActivity {

    // Save the context of the Activity
    protected Context _context;

    // for Service
    protected RuntimeService runtimeService;



    protected ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            runtimeService = ((RuntimeService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            runtimeService = null;
        }
    };

    public void doLinkService() {
        bindService(new Intent (this, RuntimeService.class), mConnection,
                Context.BIND_AUTO_CREATE);

        // Set Current Context
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                runtimeService.setCurrentContext (_context);
            }
        }, 2000);
    }

    protected void doUnbindService() {
        unbindService (mConnection);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        _context = this;

        // Start Service
        if (!isRuntimeServiceRunning (RuntimeService.class)){
            startService(new Intent(this, RuntimeService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume ();
        doLinkService ();
    }


    @Override
    protected void onPause() {
        super.onPause ();
        doUnbindService ();
    }


    private boolean isRuntimeServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
