package com.kostandin.caushi.runtimecollector.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static android.content.Context.ACTIVITY_SERVICE;

public class ServiceThread implements Runnable {

    private MyService myService;
    private Context context;

    // Utils
    private String deviceInfo;

    // Maps For Data Collected
    private HashMap<String, String> obajectValuesMap;
    private HashMap<String, String> viewMap;
    private HashMap<String, String> logTagsMap;



    ServiceThread(MyService myService){
        this.myService = myService;
    }

    @Override
    public void run() {

        // First Step get Device Info
        deviceInfo = getDeviceInfo ();


        while (true) {

            while (isPhoneLocked ()) {

                PayloadRequest req = new PayloadRequest ();

                // set DeviceInfo
                req.setDeviceInfo (deviceInfo);

                // set LOGS
                if (Configuration.EXTRACT_FULL_LOGS) {

                    req.setLog (getLogs ());
                } else if (Configuration.EXTRACT_FULL_LOGS_AND_FILTER_THEM){

                    req.setLog (filterLogs ());
                    req.setLogTags (logTagsMap);
                }

                try {
                    Thread.sleep (30000);
                    System.out.println ("TIMEOUT END");
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }
    }


    public void setContext(Context context) {

        this.context = context;

        getView (context.getClass ().toString ());

        System.out.println ("Context set to ServiceThread");
    }

    public void setViewMap(HashMap<String, String> viewMap) {

        this.viewMap = viewMap;
    }


    // Get View info, when fragments or other things inside an activity changes
    // the TAG = passed TAG string
    public void getView(String tag) {

        if (!viewMap.containsKey (tag)) {
            viewMap.put (tag, viewInfo ());
        }
    }


    // Report Object Value with tag/class
    public void reportObjectVal(String tag, String o) {

        obajectValuesMap.put (tag, o);
        System.out.print (o);
    }


    public void calledMethod(String TAG, String method) {


    }


    public boolean isPhoneLocked() {
        return false;
    }










    private String getDeviceInfo() {

        String content = ("Brand : "+ Build.BRAND +"\n"
                +"Model : "+ Build.MODEL+"\n"
                +"Build num : "+ Build.DISPLAY+"\n"
                +"CPU model : "+ getCpuInfoMap ().get ("model name")+"\n"
                +"N° processors : " + Runtime.getRuntime ().availableProcessors () + "\n");

        return content;
    }
    private Map<String, String> getCpuInfoMap() {
        Map<String, String> map = new HashMap<String, String> ();
        try {
            Scanner s = new Scanner(new File ("/proc/cpuinfo"));
            while (s.hasNextLine()) {
                String[] vals = s.nextLine().split(": ");
                if (vals.length > 1) map.put(vals[0].trim(), vals[1].trim());
            }
        } catch (Exception e) {
            Log.e("getCpuInfoMap",Log.getStackTraceString(e));}
        return map;
    }



    private String getCPU() {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("ps");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                log.append(line);
            }
            String content = (log.toString());

            java.lang.Process process2 = Runtime.getRuntime().exec("ps -eo %cpu");
            BufferedReader reader2 = new BufferedReader(
                    new InputStreamReader(process2.getInputStream()));

            StringBuilder log2 = new StringBuilder();
            String line2 = "";
            while ((line2 = reader2.readLine()) != null) {
                log2.append(line2);
            }
            content = (content + "\n\n" + log2.toString());

            return content;

        } catch (IOException e) {
            e.printStackTrace ();
        }

        return null;
    }



    private void getRAM() {
        test = ("Total RAM : "+(float)Math.round (getAvailableMemory().totalMem/1048576/100)/10+" GB\n"
                +"Available RAM : "+(float)Math.round (getAvailableMemory().availMem/1048576)/1000+" GB\n"
                +"LowMemory RAM : " +getAvailableMemory ().lowMemory +"\n"
                +"Threshold RAM : "+(float)Math.round (getAvailableMemory().threshold/1048576)/1000+" GB\n");

        // TODO : LOGS
        Log.e ("RAM", test);

    }
    // TODO : get usedRAM
    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }



    private void getHeap() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        test = ("Available HEAP : " + (float)availHeapSizeInMB/1000 +" GB\n"
                +"Used HEAP : " + (float) usedMemInMB/1000 +" GB\n"
                +"MaxAvailable HEAP : " + (float) maxHeapSizeInMB/1000 +" GB\n");

        // TODO : LOG
        Log.e ("HEAP", test);
    }



    private void getUI() {
        test = (
                "Density : " + context.getResources().getDisplayMetrics().density + " - " + getDensityName (context) + "\n"
                        + "Density Dpi : " + context.getResources ().getDisplayMetrics ().densityDpi + "\n"
                        + "Width : " + context.getResources ().getDisplayMetrics ().widthPixels + "px - "
                        + context.getResources ().getDisplayMetrics ().widthPixels / context.getResources().getDisplayMetrics().density + "dp\n"
                        + "Height : " + (context.getResources ().getDisplayMetrics ().heightPixels + getNavigationBarHeight ()) + "px - "
                        + (context.getResources ().getDisplayMetrics ().heightPixels + getNavigationBarHeight ()) / context.getResources().getDisplayMetrics().density + "dp\n"
        );
    }
    private static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
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
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }



    private String viewInfo() {

        String output = "";

        final ViewGroup parent = (ViewGroup) ((ViewGroup) ((Activity)context)
                .findViewById(android.R.id.content)).getChildAt(0);
        showViewInfo (parent, output);

        return output;
    }
    private void showViewInfo(View view, String output) {
        try {
            if (view.getId () != 0xffffffff)
                output = (output +"id:" +view.getResources ().getResourceName (view.getId ())+"\n");
        } catch (NullPointerException exception){}
        try {
            output = (output+"class:" + view.getClass ().toString () + "\n");
        } catch (NullPointerException exception){}
        output = (output+"x:" +
                view.getX () + "\ny:" + view.getY () + "\nwidth:" + view.getWidth () +
                "\nheight:" + view.getHeight ()+"\n\n");

        if((view instanceof ViewGroup)) {
            //find the children
            for (int i = 0; i < ((ViewGroup) view).getChildCount (); i++) {
                showViewInfo (((ViewGroup) view).getChildAt (i), output);
            }
        }
    }



    private void clearLogs() {
        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
        }
        catch (IOException e) {}
    }
    private String getLogs() {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }

            clearLogs ();
            return (log.toString());
        }
        catch (IOException e) {}

        return null;
    }
    private String filterLogs() {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";

            logTagsMap = new HashMap<> ();

            while ((line = bufferedReader.readLine()) != null) {

                log.append (line);

                //CHECK YOUR TAG HERE
                for (String s : Configuration.getLogsFilters ()) {

                    if(line.contains(s)) {

                        logTagsMap.put (s, line);
                    }
                }
            }

            clearLogs ();
            return (log.toString());
        }
        catch (IOException e) {}

        return null;
    }



    // Get intents
    private void getIntents() {

        // TODO : build a Broadcast Receiver
    }
}
