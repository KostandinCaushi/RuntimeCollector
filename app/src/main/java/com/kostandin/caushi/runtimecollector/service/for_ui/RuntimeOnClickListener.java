package com.kostandin.caushi.runtimecollector.service.for_ui;

import android.view.View;

import com.kostandin.caushi.runtimecollector.service.RuntimeService;

import java.util.HashMap;

public class RuntimeOnClickListener implements View.OnClickListener {


    private RuntimeService service;
    private String tag;
    private HashMap<String, String> methodData;


    public RuntimeOnClickListener(RuntimeService service, String tag) {
        this.service = service;
        this.tag = tag;
    }

    public RuntimeOnClickListener(RuntimeService service, String tag, HashMap<String, String> methodData) {
        this.service = service;
        this.tag = tag;
        this.methodData = methodData;
    }


    @Override
    public void onClick(View v) {

        service.calledTouchMethod (tag, methodData);
    }
}
