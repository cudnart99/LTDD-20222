package com.android.slap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SocratesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socrates);

        Timer timerRefresh = new Timer();
        timerRefresh.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                System.out.println(isNetworkConnected() ? "connected" : "disconnect");
            }
        }, 0, 1000);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}