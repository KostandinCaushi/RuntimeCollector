package com.kostandin.caushi.runtimecollector.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

    ServiceThread serviceThread;
    Context currentContext;


    private final IBinder myBinder = new LocalBinder();


    public class LocalBinder extends Binder {

        public MyService getService() {

            System.out.println("I am in Localbinder");
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.serviceThread = new ServiceThread (this);
        new Thread(serviceThread).start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        System.out.println("Binded to Service");
        return myBinder;
    }


    // Set Context when activity changes
    public void setCurrentContext(Context currentContext) {

        this.currentContext = currentContext;
        serviceThread.setContext (currentContext);
    }

    // Get View info, when fragments or other things inside an activity changes
    // the TAG = hashcode
    public void getView() {
        // TODO convert to hash
        // serviceThread.getView (tag);
    }

    // Get View info, when fragments or other things inside an activity changes
    // the TAG = passed TAG string
    public void getView(String tag) {
        serviceThread.getView (tag);
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
