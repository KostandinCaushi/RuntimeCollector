package com.kostandin.caushi.runtimecollector.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kostandin.caushi.runtimecollector.R;
import com.kostandin.caushi.runtimecollector.service.MyService;

public class Activity1 extends AppCompatActivity {

    private Context _context;

    // For Fragments
    private Fragment fragment;

    // for Service
    private MyService myService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };

    public void doBindService() {
        bindService(new Intent(Activity1.this, MyService.class), mConnection,
                Context.BIND_AUTO_CREATE);

        // Set Current Context
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                myService.setCurrentContext (_context);
            }
        }, 1000);
    }

    public void doUnbindService() {
        unbindService (mConnection);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        _context = this;
        setContentView (R.layout.activity1);

        // Set Fragment
        fragment = new Fragment1 ();
        setFragment (fragment);

        // Bind Service
        doBindService ();
    }

    @Override
    protected void onResume() {
        super.onResume ();
        doBindService ();
    }


    @Override
    protected void onPause() {
        super.onPause ();
        doUnbindService ();
    }


    private void setFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (R.id.main_frame, f);
        fragmentTransaction.commit ();
    }

    public void setF1(View view) {
        fragment = new Fragment1 ();
        setFragment (fragment);
    }

    public void setF2(View view) {
        fragment = new Fragment2 ();
        setFragment (fragment);
    }
}
