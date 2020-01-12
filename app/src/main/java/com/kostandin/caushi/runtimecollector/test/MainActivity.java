package com.kostandin.caushi.runtimecollector.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kostandin.caushi.runtimecollector.R;
import com.kostandin.caushi.runtimecollector.service.RuntimeService;
import com.kostandin.caushi.runtimecollector.service.for_ui.RuntimeActivity;
import com.kostandin.caushi.runtimecollector.service.for_ui.RuntimeOnClickListener;

import java.util.HashMap;

public class MainActivity extends RuntimeActivity {

    // Layout Component
    private Button info_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity_main);

        // Set Listener
        info_bt = (Button) findViewById (R.id.info_bt);

        final HashMap<String, String> dataForBt = new HashMap<> ();
        dataForBt.put ("Var :", "123456");

        // Need a delay, in order to wait till the service starts
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                info_bt.setOnClickListener (new RuntimeOnClickListener(runtimeService,"TAG1", dataForBt) {

                    @Override
                    public void onClick(View v) {
                        super.onClick (info_bt);

                        // rest of the code
                        System.out.print ("PIPPO");
                    }
                });
            }
        }, 1000);
    }


    public void goToActivity1(View view) {
        Intent intent = new Intent (this, Activity1.class);
        startActivity (intent);
    }

    public void sendIntent(View view) {
        Intent intent = new Intent ();
        intent.setAction ("PIPPO");
        intent.putExtra ("PIPPO", "ciao");
        sendBroadcast (intent);
    }
}
