package com.kostandin.caushi.runtimecollector.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class MyService extends Service {

    ServiceThread serviceThread;
    Context currentContext;

    // Utils
    private final static String NOT_SUCCEED = "Not succeed";
    private final static String VIEW_PREF = "viewData";
    private final static String STRING_MAP = "stringMap";
    private SharedPreferences viewSharedPrefs;
    private HashMap<String, String> viewsMap;


    private final IBinder myBinder = new LocalBinder();


    public class LocalBinder extends Binder {

        public MyService getService() {

            System.out.println("I am in Localbinder");
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        loadViewMap ();

        this.serviceThread = new ServiceThread (this);
        new Thread(serviceThread).start();

        serviceThread.setViewMap (viewsMap);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        System.out.println("Binded to Service");
        return myBinder;
    }


    private void loadViewMap() {

        viewSharedPrefs = getSharedPreferences (VIEW_PREF, Context.MODE_PRIVATE);

        if (viewSharedPrefs != null) {
            Gson gson = new Gson ();
            String storedMapString = viewSharedPrefs.getString (STRING_MAP, NOT_SUCCEED);
            if (!storedMapString.equals (NOT_SUCCEED)) {
                viewsMap = gson.fromJson (storedMapString, new TypeToken<HashMap<String, String>> () {
                }.getType ());
            }
        }

        System.out.println ("VIEW MAP LOADED");
    }

    private void saveViewMap() {

        viewSharedPrefs = getSharedPreferences (VIEW_PREF, Context.MODE_PRIVATE);

        if (viewSharedPrefs != null) {
            Gson gson = new Gson ();
            String storedMapString = gson.toJson (viewsMap);
            SharedPreferences.Editor editor = viewSharedPrefs.edit ();
            editor.putString (STRING_MAP, storedMapString);
            editor.apply ();
        }

        System.out.println ("VIEW MAP SAVED");
    }


    // Set Context when activity changes
    public void setCurrentContext(Context currentContext) {

        this.currentContext = currentContext;
        serviceThread.setContext (currentContext);
    }

    // Get View info, when fragments or other things inside an activity changes
    // @param tag = tag assigned to that view
    public void getView(String tag) {

        if (viewsMap != null && !viewsMap.containsKey (tag)) {
            serviceThread.getView (tag);
            viewsMap.put (tag, "");
            saveViewMap ();
            return;
        }

        System.out.println ("VIEW ALREADY PRESENT");
    }

    // Report Object Value
    public void reportObjectVal(Object o) {
        serviceThread.reportObjectVal (o.getClass ().toString (), o.toString ());
    }

    // Report Object Value with TAG
    public void reportObjectVal(Object o, String tag) {
        serviceThread.reportObjectVal (tag, o.toString ());
    }

    public void calledMethod(String TAG, String method) {


    }
}
