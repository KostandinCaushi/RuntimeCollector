package com.kostandin.caushi.runtimecollector.service;

import java.util.HashMap;
import java.util.Map;

public class PayloadRequest {


    // Device Info
    private String deviceInfo;

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
}
