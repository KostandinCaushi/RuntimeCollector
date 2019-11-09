package com.kostandin.caushi.runtimecollector.service.for_ui;

import android.view.View;

import com.kostandin.caushi.runtimecollector.service.RuntimeService;

import java.util.HashMap;

public class RuntimeOnLocgClickListener implements View.OnLongClickListener {

    private RuntimeService service;
    private String tag;
    private HashMap<String, String> methodData;


    public RuntimeOnLocgClickListener(RuntimeService service, String tag) {
        this.service = service;
        this.tag = tag;
    }

    public RuntimeOnLocgClickListener(RuntimeService service, String tag, HashMap<String, String> methodData) {
        this.service = service;
        this.tag = tag;
        this.methodData = methodData;
    }


    @Override
    public boolean onLongClick(View v) {
        service.calledTouchMethod (tag, methodData);
        return false;
    }
}
