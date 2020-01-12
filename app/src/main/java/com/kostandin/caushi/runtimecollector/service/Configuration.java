package com.kostandin.caushi.runtimecollector.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configuration {


    // Time in Seconds
    public static final String INTERVAL = "interval";
    // SharedPreferences
    public static final String GET_PREFERENCES = "getPreferences";
    // CPU Usage
    public static final String GET_CPU_USAGE = "getCpuUsage";
    // RAM Usage
    public static final String GET_RAM_USAGE = "getRamUsage";
    // HEAP Usage
    public static final String GET_HEAP_USAGE = "getHeapUsage";
    // UI Info
    public static final String GET_UI_INFO = "getUiInfo";
    public static final String GET_VIEW = "getView";
    // Methods
    public static final String GET_METHODS = "getMethods";
    public static final String GET_TOUCH_METHODS = "getTouchMethods";
    // Obj values
    public static final String GET_OBJ_VALUES = "getObjValues";
    // Logs
    public static final String GET_LOGS = "getLogs";
    public static final String LOG_FILTERS = "LogFilters";
    // Intents
    public static final String GET_INTENTS = "getIntents";
    public static final String INTENT_FILTERS = "IntentFilters";
    // REST call data
    public static final String URL = "url";



    public HashMap<String, String> configurations = new HashMap<String, String>() {{
        put (INTERVAL,"1800");
        put (GET_PREFERENCES, "false");
        put (GET_CPU_USAGE, "false");
        put (GET_RAM_USAGE, "false");
        put (GET_HEAP_USAGE, "false");
        put (GET_UI_INFO, "true");
        put (GET_VIEW, "false");
        put (GET_METHODS, "false");
        put (GET_TOUCH_METHODS, "false");
        put (GET_OBJ_VALUES, "false");
        put (GET_LOGS, "false");
        put (GET_INTENTS, "true");
        put (URL, "www.url.com:8989");
    }};

    public HashMap<String, List<String>> filters = new HashMap<String, List<String>>() {{

        // Logs
        List<String> logFilters = new ArrayList<> ();
//        logFilters.add ("something");
        put (LOG_FILTERS, logFilters);

        // Intents
        List<String> intentFilters = new ArrayList<> ();
        intentFilters.add ("PIPPO");
        put (INTENT_FILTERS, intentFilters);
    }};



    public HashMap<String, String> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(HashMap<String, String> configurations) {
        this.configurations = configurations;

    }

    public HashMap<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(HashMap<String, List<String>> filters) {
        this.filters = filters;
    }
}
