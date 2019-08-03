package com.kostandin.caushi.runtimecollector.test;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kostandin.caushi.runtimecollector.R;
import com.kostandin.caushi.runtimecollector.service.MyService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Context _context;


    // for Service
    private MyService myService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((MyService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };

    public void doBindService() {
        bindService(new Intent(MainActivity.this, MyService.class), mConnection,
                Context.BIND_AUTO_CREATE);

        // Set Current Context
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                myService.setCurrentContext (_context);
            }
        }, 1000);
    }

    public void doUnbindService() {
        unbindService (mConnection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        _context = this;
        setContentView (R.layout.activity_main);

        // Get components
        textView = findViewById (R.id.text_display);

        // Start Service
        startService(new Intent(MainActivity.this, MyService.class));
        doBindService ();
    }

    @Override
    protected void onResume() {
        super.onResume ();
        doBindService ();
    }


    @Override
    protected void onPause() {
        super.onPause ();
        doUnbindService ();
    }



    public void getDeviceInfo(View view) {
        textView.setText ("Brand : "+ Build.BRAND +"\n"
                +"Model : "+ Build.MODEL+"\n"
                +"Build num : "+ Build.DISPLAY+"\n"
                +"CPU model : "+ getCpuInfoMap ().get ("model name")+"\n"
                +"N° processors : " + Runtime.getRuntime ().availableProcessors () + "\n");

        // TODO : called report value
        myService.reportObjectVal (textView.getText ());
    }
    public Map<String, String> getCpuInfoMap() {
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



    public void getCPU(View view) {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("ps");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                //CHECK YOUR MSG HERE
                //if(line.contains("CPU")) {
                log.append(line);
                //}
            }
            textView.setText(log.toString());

            java.lang.Process process2 = Runtime.getRuntime().exec("ps -eo %cpu");
            BufferedReader reader2 = new BufferedReader(
                   new InputStreamReader(process2.getInputStream()));

            StringBuilder log2 = new StringBuilder();
            String line2 = "";
            while ((line2 = reader2.readLine()) != null) {
                //CHECK YOUR MSG HERE
                //if(line.contains("CPU")) {
                log2.append(line2);
                //}
            }
            textView.setText(textView.getText () + "\n\n" + log2.toString());

            // TODO : called report value
            myService.reportObjectVal (textView.getText ());

        } catch (IOException e) {
            e.printStackTrace ();
        }
    }



    public void getRAM(View view) {
        textView.setText ("Total RAM : "+(float)Math.round (getAvailableMemory().totalMem/1048576/100)/10+" GB\n"
                +"Available RAM : "+(float)Math.round (getAvailableMemory().availMem/1048576)/1000+" GB\n"
                +"LowMemory RAM : " +getAvailableMemory ().lowMemory +"\n"
                +"Threshold RAM : "+(float)Math.round (getAvailableMemory().threshold/1048576)/1000+" GB\n");
    }
    // TODO : get usedRAM
    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }



    public void getHeap(View view) {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        textView.setText ("Available HEAP : " + (float)availHeapSizeInMB/1000 +" GB\n"
                +"Used HEAP : " + (float) usedMemInMB/1000 +" GB\n"
                +"MaxAvailable HEAP : " + (float) maxHeapSizeInMB/1000 +" GB\n");
    }



    public void getUI(View view) {
        textView.setText (
                "Density : " + getResources().getDisplayMetrics().density + " - " + getDensityName (this) + "\n"
                + "Density Dpi : " + getResources ().getDisplayMetrics ().densityDpi + "\n"
                + "Width : " + getResources ().getDisplayMetrics ().widthPixels + "px - "
                        + getResources ().getDisplayMetrics ().widthPixels/getResources().getDisplayMetrics().density + "dp\n"
                + "Height : " + (getResources ().getDisplayMetrics ().heightPixels + getNavigationBarHeight ()) + "px - "
                        + (getResources ().getDisplayMetrics ().heightPixels + getNavigationBarHeight ()) /getResources().getDisplayMetrics().density + "dp\n"
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
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }



    public void viewInfo(View view) {
        textView.setText ("");
        final ViewGroup parent = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        showViewInfo (parent);
    }
    private void showViewInfo(View view) {
        try {
            if (view.getId () != 0xffffffff)
            textView.setText (textView.getText ()+"id:" +view.getResources ().getResourceName (view.getId ())+"\n");
        } catch (NullPointerException exception){}
        try {
            textView.setText (textView.getText ()+"class:" + view.getClass ().toString () + "\n");
        } catch (NullPointerException exception){}
        textView.setText (textView.getText ()+"x:" +
                view.getX () + "\ny:" + view.getY () + "\nwidth:" + view.getWidth () +
                "\nheight:" + view.getHeight ()+"\n\n");

        if((view instanceof ViewGroup)) {
            //find the children
            for (int i = 0; i < ((ViewGroup) view).getChildCount (); i++) {
                showViewInfo (((ViewGroup) view).getChildAt (i));
            }
        }
    }



    public void clearLog(View view) {
        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
        }
        catch (IOException e) {}
    }
// Contains also a filter for a specific String to search
    public void getLog(View view) {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                //CHECK YOUR MSG HERE
                //if(line.contains("CPU")) {
                    log.append(line);
                //}
            }
            textView.setText(log.toString());
        }
        catch (IOException e) {}
    }



    // Get intents
    public void getIntents(View view) {

        // TODO : build a Broadcast Receiver
    }
    public void goToActivity1(View view) {
        Intent intent = new Intent (this, Activity1.class);
        startActivity (intent);
    }
}
