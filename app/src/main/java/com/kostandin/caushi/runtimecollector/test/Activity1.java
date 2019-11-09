package com.kostandin.caushi.runtimecollector.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.kostandin.caushi.runtimecollector.R;
import com.kostandin.caushi.runtimecollector.service.for_ui.RuntimeActivity;

public class Activity1 extends RuntimeActivity {

    // For Fragments
    private Fragment fragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity1);

        // Set Current Context
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set Fragment
                fragment = new Fragment1 (runtimeService);
                setFragment (fragment);
            }
        }, 1000);
    }


    private void setFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager ().beginTransaction ();
        fragmentTransaction.replace (R.id.main_frame, f);
        fragmentTransaction.commit ();
    }

    public void setF1(View view) {
        fragment = new Fragment1 (runtimeService);
        setFragment (fragment);
    }

    public void setF2(View view) {
        fragment = new Fragment2 (runtimeService);
        setFragment (fragment);
    }
}
