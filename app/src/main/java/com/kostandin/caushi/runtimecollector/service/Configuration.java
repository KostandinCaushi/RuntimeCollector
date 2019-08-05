package com.kostandin.caushi.runtimecollector.service;

import java.util.ArrayList;
import java.util.List;

public class Configuration {


    // Time in Seconds
    public static final int INTERVAL = 30;


    // Logs
    public static final boolean EXTRACT_FULL_LOGS = false;
    public static final boolean EXTRACT_FULL_LOGS_AND_FILTER_THEM = false;

    // Logs Filters/Tags
    public static List<String> getLogsFilters() {

        List<String> filters = new ArrayList<> ();

        // Ex.      filters.add ("TAG");

        return filters;
    }



    // CPU Usage
    public static final boolean CPU_USAGE = false;

    // RAM Usage
    public static final boolean RAM_USAGE = false;

    // HEAP Usage
    public static final boolean HEAP_USAGE = false;



    // UI Info
    public static final boolean UI_INFO = false;



    // Intents
    public static final boolean GET_INTENTS = false;

    public static List<String> getIntentFilters() {

        List<String> filters = new ArrayList<> ();

        // Ex.      filters.add ("TAG");

        return filters;
    }



    // REST call data
    public static final String URL = "";
}
