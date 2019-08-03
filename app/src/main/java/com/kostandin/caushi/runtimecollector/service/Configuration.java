package com.kostandin.caushi.runtimecollector.service;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    // LOGS
    public static final boolean EXTRACT_FULL_LOGS = false;
    public static final boolean EXTRACT_FULL_LOGS_AND_FILTER_THEM = false;

    // LOGS FILTERS/TAGS
    public static List<String> getLogsFilters() {

        List<String> filters = new ArrayList<> ();

        // Ex.      filters.add ("TAG");

        return filters;
    }



    // CPU USAGE
    public static final boolean CPU_USAGE = false;

    // RAM USAGE
    public static final boolean RAM_USAGE = false;

    // HEAP USAGE
    public static final boolean HEAP_USAGE = false;
}
