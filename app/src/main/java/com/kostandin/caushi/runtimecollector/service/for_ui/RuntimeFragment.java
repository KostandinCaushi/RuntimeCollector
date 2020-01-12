package com.kostandin.caushi.runtimecollector.service.for_ui;

import android.os.Handler;
import android.support.v4.app.Fragment;

import com.kostandin.caushi.runtimecollector.service.RuntimeService;

public class RuntimeFragment extends Fragment {

    // Runtime Service
    protected RuntimeService service;

    protected String tag;


    public RuntimeFragment() {}


    @Override
    public void onResume() {
        super.onResume ();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                service.getView (tag);
            }
        }, 1000);
    }

    public RuntimeService getService() {
        return service;
    }

    public void setService(RuntimeService service) {
        this.service = service;
    }
}
