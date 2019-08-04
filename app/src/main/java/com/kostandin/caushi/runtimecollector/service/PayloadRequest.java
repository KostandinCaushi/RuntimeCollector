package com.kostandin.caushi.runtimecollector.service;

import java.util.Map;

public class PayloadRequest {


    // Device Info
    private String deviceInfo;
    private String uiInfo;

    // LOGS
    private String log;
    private Map<String, String> logTags;

    // Maps For Data Collected
    private Map<String, String> obajectValuesMap;





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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Map<String, String> getLogTags() {
        return logTags;
    }

    public void setLogTags(Map<String, String> logTags) {
        this.logTags = logTags;
    }

    public Map<String, String> getObajectValuesMap() {
        return obajectValuesMap;
    }

    public void setObajectValuesMap(Map<String, String> obajectValuesMap) {
        this.obajectValuesMap = obajectValuesMap;
    }
}
