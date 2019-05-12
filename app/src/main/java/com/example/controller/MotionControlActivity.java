package com.example.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MotionControlActivity extends AppCompatActivity {

    private Sensor gravitySensor;
    private SensorManager gravitySensorManager;
    private TcpClient mTcpClient;
    Button connectButton;
    int joy1xStrength,joy1yStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);
        buttonListener();

        gravitySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = gravitySensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gravitySensorManager.registerListener(gravitySensorEventListener, gravitySensor,SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if(mTcpClient!=null){
            mTcpClient.stopClient();
        }
    }

    public void buttonListener(){
        connectButton = findViewById(R.id.MC_connect_button);
        connectButton.setOnClickListener(v->{
            if(mTcpClient != null) {
                if (!mTcpClient.ismRun()) {
                    new MotionControlActivity.ConnectTask().execute("");
                } else {
                    mTcpClient.stopClient();
                }
            }else {
                new MotionControlActivity.ConnectTask().execute("");
            }

        });
        final Button upTrim = findViewById(R.id.upTrim);
        upTrim.setOnClickListener(v -> {
            if(mTcpClient != null) {
                mTcpClient.sendMessage("TRU");
            }
        });
        final Button downTrim = findViewById(R.id.downTrim);
        downTrim.setOnClickListener(v -> {
            if(mTcpClient != null) {
                mTcpClient.sendMessage("TRD");
            }
        });
        final Button leftTrim = findViewById(R.id.leftTrim);
        leftTrim.setOnClickListener(v -> {
            if(mTcpClient != null) {
                mTcpClient.sendMessage("TRL");
            }
        });
        final Button rigthTrim = findViewById(R.id.rigthTrim);
        rigthTrim.setOnClickListener(v -> {
            if(mTcpClient != null) {
                mTcpClient.sendMessage("TRR");
            }
        });
    }

        private SensorEventListener gravitySensorEventListener = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent event) {
            int xStrength =  (int)Math.round(-15* event.values[1]);
            int yStrength =  (int)Math.round(-15 * event.values[0]);
            if(xStrength<-100) xStrength=-100;
            if(xStrength>100) xStrength=100;
            if(yStrength<-100) yStrength=-100;
            if(yStrength>100) yStrength=100;
            if(mTcpClient != null) {
                mTcpClient.sendMessage("JX2 " + xStrength + " JY2 " + yStrength);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            mTcpClient = new TcpClient(msg-> {
                publishProgress(msg);
            });
            mTcpClient.run(getApplicationContext(),connectButton);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            System.out.println("Response: "+values[0]);
        }
    }
}
