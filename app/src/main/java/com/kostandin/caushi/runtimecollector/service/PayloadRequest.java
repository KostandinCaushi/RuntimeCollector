package com.kostandin.caushi.runtimecollector.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayloadRequest {


    // Device Info
    private String deviceInfo;
    private String uiInfo;

    // Preferences
    private Map<String, ?> preferences;

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

    // UI
    private HashMap<String, String> viewMap;
    private List<String> viewFlow;

    private List<String> methodsAndViewFlow;

    // Methods
    private HashMap<String, HashMap<String, String>> methodsMap;         // Contains also touchMethods
    private HashMap<String, HashMap<String, String>> touchMethodsMap;



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

    public Map<String, ?> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, ?> preferences) {
        this.preferences = preferences;
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

    public HashMap<String, HashMap<String, String>> getMethodsMap() {
        return methodsMap;
    }

    public void setMethodsMap(HashMap<String, HashMap<String, String>> methodsMap) {
        this.methodsMap = methodsMap;
    }

    public HashMap<String, HashMap<String, String>> getTouchMethodsMap() {
        return touchMethodsMap;
    }

    public void setTouchMethodsMap(HashMap<String, HashMap<String, String>> touchMethodsMap) {
        this.touchMethodsMap = touchMethodsMap;
    }

    public List<String> getViewFlow() {
        return viewFlow;
    }

    public void setViewFlow(List<String> viewFlow) {
        this.viewFlow = viewFlow;
    }

    public List<String> getMethodsAndViewFlow() {
        return methodsAndViewFlow;
    }

    public void setMethodsAndViewFlow(List<String> methodsAndViewFlow) {
        this.methodsAndViewFlow = methodsAndViewFlow;
    }
}
