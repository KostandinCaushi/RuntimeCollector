package com.kostandin.caushi.runtimecollector.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ServiceThread implements Runnable {

    private RuntimeService runtimeService;
    private Context context;
    private Configuration configuration;

    // Device Info
    private String deviceInfo;
    private HashMap<String, Map<String, ?>> preferences = new HashMap<> ();
    private String uiInfo;

    // Maps For Data Collected
    private HashMap<String, String> objectValuesMap = new HashMap<> ();
    private HashMap<String, String> logTagsMap = new HashMap<> ();
    private HashMap<String, String> intentsMap = new HashMap<> ();

    // UI
    private final static String NOT_SUCCEED = "Not succeed";
    private final static String VIEW_PREF = "viewFile";
    private final static String VIEW_STRIN_MAP = "viewStrinMap";
    private HashMap<String, String> viewMap = new HashMap<> ();
    private HashMap<String, String> newViewMap = new HashMap<> ();
    private boolean savedNewView;
    private List<String> viewFlow = new ArrayList<> ();

    private List<String> methodsAndViewFlow = new ArrayList<> ();

    // Methods
    private HashMap<String, HashMap<String, String>> methodsMap = new HashMap<> ();         // Contains also touchMethods
    private HashMap<String, HashMap<String, String>> touchMethodsMap = new HashMap<> ();


    ServiceThread(RuntimeService runtimeService, Configuration configuration) {
        this.runtimeService = runtimeService;
        this.configuration = configuration;
    }

    @Override
    public void run() {

        // Initialize Broadcast Receiver
        if (Boolean.parseBoolean (configuration.getConfigurations ().get (configuration.GET_INTENTS))) {
            mMessageReceiver = new BroadcastReceiver () {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String key = extractKey (intent);

                    if (!configuration.getFilters ().isEmpty ()) {
                        for (String s : configuration.getFilters ().get (Configuration.INTENT_FILTERS)) {

                            if (key.equals (s)) {

                                String content = ("Action : " + intent.getAction () + "\n"
                                        + "Component : " + (intent.getComponent () != null ? intent.getComponent ().getClassName () : "") + "\n"
                                        + "Type : " + intent.getType () + "\n"
                                        + "DataString : " + intent.getDataString () + "\n"
                                        + "SerializableExtra : " + getOutputSerizlizableExtra (intent, s) + "\n"
                                        + "BundleExtra : " + (intent.getBundleExtra (s) != null ? intent.getBundleExtra (s).toString () : "") + "\n"
                                        + "\nTimestamp : " + new Date ().toString ());

                                String filter = s;
                                for (int i = 1; intentsMap.containsKey (filter); i++) {
                                    filter = s + i;
                                }

                                intentsMap.put (filter, content);
                            }
                        }
                    }
                }
            };

            // Register Receiver Filters
            if (configuration.getFilters ().get (configuration.INTENT_FILTERS) != null &&
                    !configuration.getFilters ().get (configuration.INTENT_FILTERS).isEmpty ())
                for (String s : configuration.getFilters ().get (configuration.INTENT_FILTERS)) {
                    context.registerReceiver(mMessageReceiver, new IntentFilter (s));
                }
        }

        try {
            Thread.sleep (10000);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        // Step 1 : get Device Info
        deviceInfo = getDeviceInfo ();

        // Step 2 : get all sharedPreferences
        if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_PREFERENCES))) {
            loadSharedPreferences();
        }

        // Step 3 : get UI info
        if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_UI_INFO))) {
            uiInfo = getUI ();
        }

        // Step 4 : load viewMap from SharedPreferences
        loadViewMap ();


        while (true) {

            while (context != null && isPhoneLocked ()) {

                PayloadRequest req = new PayloadRequest ();

                // set DeviceInfo
                req.setDeviceInfo (deviceInfo);

                // set uiInfo
                if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_UI_INFO))) {
                    req.setUiInfo (uiInfo);
                }

                // set preferences
                if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_PREFERENCES))) {
                    req.setPreferences (preferences);
                }

                // set LOGS
                if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_LOGS))) {
                    req.setLog (getLogs ());
                    req.setLogTagsMap (logTagsMap);
                }

                // CPU
                if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_CPU_USAGE))) {
                    req.setCpuUsage (getCPU ());
                }

                // RAM
                if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_RAM_USAGE))) {
                    req.setRamUsage (getRAM ());
                }

                // HEAP
                if (Boolean.parseBoolean (configuration.getConfigurations().get (Configuration.GET_HEAP_USAGE))) {
                    req.setHeapUsage (getHeap ());
                }

                // Set Maps
                if (!objectValuesMap.isEmpty ()) {
                    req.setObjectValuesMap (objectValuesMap);
                }
                if (!intentsMap.isEmpty ()) {
                    req.setIntentsMap (intentsMap);
                }
                if (savedNewView) {
                    req.setViewMap (newViewMap);
                    saveViewMap ();
                }
                if (!methodsAndViewFlow.isEmpty ()) {
                    req.setMethodsAndViewFlow (methodsAndViewFlow);
                }
                if (!viewFlow.isEmpty ()) {
                    req.setViewFlow (viewFlow);
                }
                if (!methodsMap.isEmpty ()) {
                    req.setMethodsMap (methodsMap);
                }
                if (!touchMethodsMap.isEmpty ()) {
                    req.setTouchMethodsMap (touchMethodsMap);
                }

                // Send DATA
                try {
                    sendData (req);
                } catch (IOException e) {
                    e.printStackTrace ();
                }


                // Clear all Maps
                touchMethodsMap.clear ();
                methodsMap.clear ();
                viewFlow.clear ();
                methodsAndViewFlow.clear ();
                newViewMap.clear ();
                intentsMap.clear ();
                objectValuesMap.clear ();
                logTagsMap.clear ();



                try {

                    Thread.sleep (Integer.parseInt (configuration.getConfigurations().get (Configuration.INTERVAL)));
                    System.out.println ("TIMEOUT END");

                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setContext(Context context) {

        this.context = context;
    }

    public void loadSharedPreferences() {
        File prefsdir = new File(context.getApplicationInfo().dataDir,"shared_prefs");

        if(prefsdir.exists() && prefsdir.isDirectory()) {
            String[] list = prefsdir.list ();

            for (String s : list) {
                //remove .xml from the file name
                String preffile = s.substring (0, s.length () - 4);
                SharedPreferences sp = context.getSharedPreferences (preffile, MODE_PRIVATE);
                preferences.put (preffile, sp.getAll ());
            }
        }
    }

    private void loadViewMap() {

        if (Boolean.parseBoolean (configuration.getConfigurations ().get (Configuration.GET_VIEW))) {

            SharedPreferences viewSharedPrefs = context.getSharedPreferences (VIEW_PREF, Context.MODE_PRIVATE);

            if (viewSharedPrefs != null) {
                Gson gson = new Gson ();
                String storedMapString = viewSharedPrefs.getString (VIEW_STRIN_MAP, NOT_SUCCEED);
                if (!storedMapString.equals (NOT_SUCCEED)) {
                    viewMap = gson.fromJson (storedMapString, new TypeToken<HashMap<String, String>> () {
                    }.getType ());
                } else {
                    viewMap = new HashMap<> ();
                }
            }

            System.out.println ("VIEW MAP LOADED");
        }
    }

    public boolean isPhoneLocked() {
        boolean isLocked = false;

        // First we check the locked state
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService (Context.KEYGUARD_SERVICE);
        boolean inKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode ();

        if (inKeyguardRestrictedInputMode) {
            isLocked = true;

        } else {
            // If password is not set in the settings, the inKeyguardRestrictedInputMode() returns false,
            // so we need to check if screen on for this case

            PowerManager powerManager = (PowerManager) context.getSystemService (Context.POWER_SERVICE);
            isLocked = !powerManager.isInteractive ();

        }
        return isLocked;
    }


    // Get View info, when fragments or other things inside an activity changes
    // the TAG = passed TAG string
    public void getView(String tag) {

        if (Boolean.parseBoolean (configuration.getConfigurations ().get (Configuration.GET_VIEW))) {
            if (viewMap != null && !viewMap.containsKey (tag)) {
                savedNewView = true;
                viewMap.put (tag, viewInfo ());
                newViewMap.put (tag, viewMap.get (tag));
                viewFlow.add (tag);
                methodsAndViewFlow.add (tag);

                System.out.println ("Saved new view : " + tag);
            } else {
                viewFlow.add (tag);
                methodsAndViewFlow.add (tag);
            }
        }
    }


    private void saveViewMap() {

        SharedPreferences viewSharedPrefs = context.getSharedPreferences (VIEW_PREF, MODE_PRIVATE);

        if (viewSharedPrefs != null) {
            Gson gson = new Gson ();
            String storedMapString = gson.toJson (viewMap);
            SharedPreferences.Editor editor = viewSharedPrefs.edit ();
            editor.putString (VIEW_STRIN_MAP, storedMapString);
            editor.apply ();
        }

        savedNewView = false;

        System.out.println ("VIEW MAP SAVED");
    }


    // Report Object Value with tag/class
    public void reportObjectVal(String tag, String o) {

        objectValuesMap.put (tag, o);
        System.out.print (o);
    }


    public void calledMethod(String tag, HashMap<String, String> methodData) {

        String tagValue = tag;
        int i;
        for (i = 0; methodsMap.containsKey (tagValue); i++) {
            tagValue = tag + i;
        }

        tagValue = tag + i;

        String timestamp = new Date ().toString ();
        methodData.put ("Timestamp", timestamp);

        methodsMap.put (tagValue, methodData);
        methodsAndViewFlow.add (tagValue);

        Log.i (tag, methodData.toString ());
    }

    public void calledTouchMethod(String tag, HashMap<String, String> methodData) {

        String tagValue = tag;
        int i;
        for (i = 0; methodsMap.containsKey (tagValue); i++) {
            tagValue = tag + i;
        }

        tagValue = tag + i;

        String timestamp = new Date ().toString ();
        methodData.put ("Timestamp", timestamp);

        touchMethodsMap.put (tagValue, methodData);
        methodsMap.put (tagValue, methodData);
        methodsAndViewFlow.add (tagValue);

        Log.e (tag, methodData.toString ());
    }


    private String getDeviceInfo() {

        String content = ("Brand : " + Build.BRAND + "\n"
                + "Model : " + Build.MODEL + "\n"
                + "Build n° : " + Build.DISPLAY + "\n"
                + "CPU model : " + getCpuInfoMap ().get ("model name") + "\n"
                + "Processors n° : " + Runtime.getRuntime ().availableProcessors () + "\n");

        return content;
    }
    private Map<String, String> getCpuInfoMap() {
        Map<String, String> map = new HashMap<String, String> ();
        try {
            Scanner s = new Scanner (new File ("/proc/cpuinfo"));
            while (s.hasNextLine ()) {
                String[] vals = s.nextLine ().split (": ");
                if (vals.length > 1) map.put (vals[0].trim (), vals[1].trim ());
            }
        } catch (Exception e) {
            Log.e ("getCpuInfoMap", Log.getStackTraceString (e));
        }
        return map;
    }


    private String getCPU() {

        try {
            java.lang.Process process = Runtime.getRuntime ().exec ("ps");
            BufferedReader reader = new BufferedReader (
                    new InputStreamReader (process.getInputStream ()));

            StringBuilder log = new StringBuilder ();
            String line = "";
            while ((line = reader.readLine ()) != null) {
                log.append (line);
            }
            String content = (log.toString ());

            java.lang.Process process2 = Runtime.getRuntime ().exec ("ps -eo %cpu");
            BufferedReader reader2 = new BufferedReader (
                    new InputStreamReader (process2.getInputStream ()));

            StringBuilder log2 = new StringBuilder ();
            String line2 = "";
            while ((line2 = reader2.readLine ()) != null) {
                log2.append (line2);
            }
            content = (content + "\n\n" + log2.toString ());

            return content;

        } catch (IOException e) {
            e.printStackTrace ();
        }

        return null;
    }

    private String getRAM() {

        String content = ("Total RAM : " + (float) Math.round (getAvailableMemory ().totalMem / 1048576 / 100) / 10 + " GB\n"
                + "Available RAM : " + (float) Math.round (getAvailableMemory ().availMem / 1048576) / 1000 + " GB\n"
                + "LowMemory RAM : " + getAvailableMemory ().lowMemory + "\n"
                + "Threshold RAM : " + (float) Math.round (getAvailableMemory ().threshold / 1048576) / 1000 + " GB\n");

        return content;

    }
    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService (ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo ();
        activityManager.getMemoryInfo (memoryInfo);
        return memoryInfo;
    }

    private String getHeap() {

        final Runtime runtime = Runtime.getRuntime ();
        final long usedMemInMB = (runtime.totalMemory () - runtime.freeMemory ()) / 1048576L;
        final long maxHeapSizeInMB = runtime.maxMemory () / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        String content = ("Available HEAP : " + (float) availHeapSizeInMB / 1000 + " GB\n"
                + "Used HEAP : " + (float) usedMemInMB / 1000 + " GB\n"
                + "MaxAvailable HEAP : " + (float) maxHeapSizeInMB / 1000 + " GB\n");

        return content;
    }


    // UI info : density, resolution
    private String getUI() {
        String content = (
                "Density : " + context.getResources ().getDisplayMetrics ().density + " - " + getDensityName (context) + "\n"
                        + "Density Dpi : " + context.getResources ().getDisplayMetrics ().densityDpi + "\n"
                        + "Width : " + context.getResources ().getDisplayMetrics ().widthPixels + "px - "
                        + context.getResources ().getDisplayMetrics ().widthPixels / context.getResources ().getDisplayMetrics ().density + "dp\n"
                        + "Height : " + (context.getResources ().getDisplayMetrics ().heightPixels + getNavigationBarHeight ()) + "px - "
                        + (context.getResources ().getDisplayMetrics ().heightPixels + getNavigationBarHeight ()) / context.getResources ().getDisplayMetrics ().density + "dp\n"
        );

        return content;
    }
    private static String getDensityName(Context context) {
        float density = context.getResources ().getDisplayMetrics ().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }
    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics ();
            ((Activity) context).getWindowManager ().getDefaultDisplay ().getMetrics (metrics);
            int usableHeight = metrics.heightPixels;
            ((Activity) context).getWindowManager ().getDefaultDisplay ().getRealMetrics (metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }


    private String viewInfo() {

        final ViewGroup parent = (ViewGroup) ((ViewGroup) ((Activity) context)
                .findViewById (android.R.id.content)).getChildAt (0);
        String output = showViewInfo (parent);

        return output;
    }
    private String showViewInfo(View view) {

        String output = "";
        try {
            if (view.getId () != 0xffffffff)
                output = (output + "id:" + view.getResources ().getResourceName (view.getId ()) + "\n");
        } catch (NullPointerException exception) {
        }
        try {
            output = (output + "class:" + view.getClass ().toString () + "\n");
        } catch (NullPointerException exception) {
        }
        output = (output + "x:" +
                view.getX () + "\ny:" + view.getY () + "\nwidth:" + view.getWidth () +
                "\nheight:" + view.getHeight () + "\n\n");

        if ((view instanceof ViewGroup)) {
            //find the children
            for (int i = 0; i < ((ViewGroup) view).getChildCount (); i++) {
                output = output + showViewInfo (((ViewGroup) view).getChildAt (i));
            }
        }
        return output;
    }


    private void clearLogs() {
        try {
            Runtime.getRuntime ().exec (new String[]{"logcat", "-c"});
        } catch (IOException e) {
        }
    }

    private String getLogs() {
        try {
            java.lang.Process process = Runtime.getRuntime ().exec ("logcat -d");
            BufferedReader bufferedReader = new BufferedReader (
                    new InputStreamReader (process.getInputStream ()));

            StringBuilder log = new StringBuilder ();
            String line = "";

            while ((line = bufferedReader.readLine ()) != null) {

                log.append (line);

                //CHECK YOUR TAG HERE
                if (!configuration.getFilters ().isEmpty ()) {
                    for (String s : configuration.getFilters ().get (Configuration.LOG_FILTERS)) {

                        if (line.contains (s)) {

                            logTagsMap.put (s, line);
                        }
                    }
                }
            }
            clearLogs ();
            return (log.toString ());
        } catch (IOException e) {
        }

        return null;
    }


    // Get Broadcast Intents
    private BroadcastReceiver mMessageReceiver;

    private String extractKey(Intent intent) {
        Set<String> keySet = Objects.requireNonNull (intent.getExtras ()).keySet ();
        Iterator iterator = keySet.iterator ();
        return (String) iterator.next ();
    }
    private String getOutputSerizlizableExtra(Intent intent, String name) {
        if (intent.getSerializableExtra (name) != null) {
            return intent.getSerializableExtra (name).toString ();
        } else if (intent.getStringExtra (name) != null) {
            return intent.getStringExtra (name);
        } else if (intent.getIntExtra (name, -1) != -1) {
            return String.valueOf (intent.getIntExtra (name, -1));
        } else {
            return String.valueOf (intent.getBooleanExtra (name, false));
        }
    }


    // Send POST request
    private void sendData(PayloadRequest req) throws IOException {

        URL url = new URL (configuration.getConfigurations ().get ((Configuration.URL)));
        HttpURLConnection con = (HttpURLConnection) url.openConnection ();
        con.setRequestMethod ("POST");

        con.setDoOutput (true);

        Gson gson = new Gson ();
        String content = gson.toJson (req);

        try (OutputStream os = con.getOutputStream ()) {
            os.write (content.getBytes ());
            os.flush ();
        }


        // Can add checks on ResponseCode
//        int responseCode = con.getResponseCode ();
    }
}
