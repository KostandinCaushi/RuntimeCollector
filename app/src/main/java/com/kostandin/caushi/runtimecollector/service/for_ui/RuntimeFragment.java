package com.kostandin.caushi.runtimecollector.service.for_ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kostandin.caushi.runtimecollector.service.RuntimeService;

public class RuntimeFragment extends Fragment {

    // Runtime Service
    protected RuntimeService service;


    public RuntimeFragment() {}

    public RuntimeFragment(RuntimeService service) {
        this.service = service;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*
        When you override this method :
        - begin : create your fragment view
        - end : call "super.onCreateView (inflater, container, savedInstanceState);"
         */

        service.getView (this.getClass ().toString ());

        return super.onCreateView (inflater, container, savedInstanceState);
    }
}
