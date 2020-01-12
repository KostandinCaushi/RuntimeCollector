package com.kostandin.caushi.runtimecollector.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kostandin.caushi.runtimecollector.R;
import com.kostandin.caushi.runtimecollector.service.RuntimeService;
import com.kostandin.caushi.runtimecollector.service.for_ui.RuntimeFragment;

public class Fragment1 extends RuntimeFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_1, container, false);

        super.onCreateView (inflater, container, savedInstanceState);

        return root;
    }
}
