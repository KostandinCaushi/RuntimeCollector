package com.kostandin.caushi.runtimecollector.service;

import java.util.HashMap;

public class PayloadRequest {


    // Device Info
    private String deviceInfo;
    private String uiInfo;

    // Performance
    private String cpuUsage;
    private String ramUsage;
    private String heapUsage;

    // LOGS
    private String log;
    private HashMap<String, String> logTagsMap;

    // Maps For Data Collected
    private HashMap<String, String> objectValuesMap;
    private HashMap<String, String> intentsMap;

    // Maps fot Methods and UI
    private HashMap<String, String> viewMap;
        // Contains also touchMethods
    private HashMap<String, String> methodsMap;
    private HashMap<String, String> touchMethodsMap;
    private HashMap<String, String> methodsAndViewMap;


    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getUiInfo() {
        return uiInfo;
    }

    public void setUiInfo(String uiInfo) {
        this.uiInfo = uiInfo;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(String ramUsage) {
        this.ramUsage = ramUsage;
    }

    public String getHeapUsage() {
        return heapUsage;
    }

    public void setHeapUsage(String heapUsage) {
        this.heapUsage = heapUsage;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public HashMap<String, String> getLogTagsMap() {
        return logTagsMap;
    }

    public void setLogTagsMap(HashMap<String, String> logTagsMap) {
        this.logTagsMap = logTagsMap;
    }

    public HashMap<String, String> getObjectValuesMap() {
        return objectValuesMap;
    }

    public void setObjectValuesMap(HashMap<String, String> objectValuesMap) {
        this.objectValuesMap = objectValuesMap;
    }

    public HashMap<String, String> getIntentsMap() {
        return intentsMap;
    }

    public void setIntentsMap(HashMap<String, String> intentsMap) {
        this.intentsMap = intentsMap;
    }

    public HashMap<String, String> getViewMap() {
        return viewMap;
    }

    public void setViewMap(HashMap<String, String> viewMap) {
        this.viewMap = viewMap;
    }

    public HashMap<String, String> getMethodsMap() {
        return methodsMap;
    }

    public void setMethodsMap(HashMap<String, String> methodsMap) {
        this.methodsMap = methodsMap;
    }

    public HashMap<String, String> getTouchMethodsMap() {
        return touchMethodsMap;
    }

    public void setTouchMethodsMap(HashMap<String, String> touchMethodsMap) {
        this.touchMethodsMap = touchMethodsMap;
    }

    public HashMap<String, String> getMethodsAndViewMap() {
        return methodsAndViewMap;
    }

    public void setMethodsAndViewMap(HashMap<String, String> methodsAndViewMap) {
        this.methodsAndViewMap = methodsAndViewMap;
    }
}
