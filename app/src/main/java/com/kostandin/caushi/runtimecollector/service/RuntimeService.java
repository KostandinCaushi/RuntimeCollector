package com.kostandin.caushi.runtimecollector.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class RuntimeService extends Service {

    private ServiceThread serviceThread;
    private Context currentContext;

    // Configurations
    private final static String CONFIGURATION = "confFile";
    private Configuration configuration;

    // View
    private final static String NOT_SUCCEED = "Not succeed";
    private final static String VIEW_PREF = "viewFile";
    private final static String VIEW_STRIN_MAP = "viewStrinMap";
    private HashMap<String, String> viewMap;


    private final IBinder myBinder = new LocalBinder();


    public class LocalBinder extends Binder {

        public RuntimeService getService() {

            System.out.println("I am in Localbinder");
            return RuntimeService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        loadConfigurations();

        loadViewMap ();

        this.serviceThread = new ServiceThread (this);
        new Thread(serviceThread).start();

        serviceThread.setConfiguration (configuration);
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    private void loadConfigurations() {

        SharedPreferences configurations = getSharedPreferences (CONFIGURATION, Context.MODE_PRIVATE);

        if (configurations != null || configurations.contains (CONFIGURATION)) {
            Gson gson = new Gson ();
            String confString = configurations.getString (CONFIGURATION, NOT_SUCCEED);
            if (!confString.equals (NOT_SUCCEED)) {
                configuration = gson.fromJson (confString, new TypeToken<Configuration> () {
                }.getType ());
            } else {
                configuration = new Configuration ();
            }
        } else {
            configuration = new Configuration ();
        }

        System.out.println ("CONFIGURATIONS LOADED");
    }
    private void saveConfigurations() {

        SharedPreferences configurations = getSharedPreferences (CONFIGURATION, Context.MODE_PRIVATE);

        if (configurations != null) {
            Gson gson = new Gson ();
            String confString = gson.toJson (configuration);
            SharedPreferences.Editor editor = configurations.edit ();
            editor.putString (CONFIGURATION, confString);
            editor.apply ();
        }

        serviceThread.setConfiguration (configuration);

        System.out.println ("CONFIGURATIONS SAVED");
    }
    public Configuration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        saveConfigurations ();
    }



    private void loadViewMap() {

        if (Boolean.parseBoolean (configuration.getConfigurations ().get (Configuration.GET_VIEW))) {

            SharedPreferences viewSharedPrefs = getSharedPreferences (VIEW_PREF, Context.MODE_PRIVATE);

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



    // Set Context when activity changes
    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
        serviceThread.setContext (currentContext);
        this.getView (currentContext.getClass ().toString ());
    }

    // Get View info, when fragments or other things inside an activity changes
    // @param tag = tag assigned to that view
    public void getView(String tag) {

        if (Boolean.parseBoolean (configuration.getConfigurations ().get (Configuration.GET_VIEW))) {
            if (viewMap != null && !viewMap.containsKey (tag)) {
                serviceThread.getView (tag);
                viewMap.put (tag, "");
                return;
            }

            Log.e ("GetView", "View already taken & saved");
        }
    }



    // Report Object Value
    public void reportObjectVal(Object o) {
        this.reportObjectVal (o.getClass ().toString (), o.toString ());
    }
    // Report Object Value with TAG
    public void reportObjectVal(Object o, String tag) {

        if (Boolean.parseBoolean (configuration.getConfigurations ().get (Configuration.GET_OBJ_VALUES))) {
            serviceThread.reportObjectVal (tag, o.toString ());
        }
    }



    public void calledMethod(String tag, HashMap<String, String> methodData) {

        if (Boolean.parseBoolean (configuration.getConfigurations ().get (Configuration.GET_METHODS))) {
            serviceThread.calledMethod (tag, methodData);
        }
    }
    public void calledMethod(String tag) {
        this.calledMethod (tag, new HashMap<String, String> ());
    }

    public void calledTouchMethod(String tag, HashMap<String, String> methodData) {

        if (Boolean.parseBoolean(configuration.getConfigurations ().get (Configuration.GET_TOUCH_METHODS))) {
            serviceThread.calledTouchMethod (tag, methodData);
        }
    }
}
