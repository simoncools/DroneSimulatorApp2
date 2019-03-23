package com.example.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MotionControlActivity extends AppCompatActivity {

    private Sensor gravitySensor;
    private SensorManager gravitySensorManager;
    private TcpClient mTcpClient;
    int joy1Angle,joy1Strength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);
        joystickListener();
        buttonListener();

        gravitySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = gravitySensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gravitySensorManager.registerListener(gravitySensorEventListener, gravitySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onDestroy() {
        super.onDestroy();
        if(mTcpClient!=null){
            mTcpClient.stopClient();
        }
    }

    public void buttonListener(){
        Button connectButton = findViewById(R.id.MC_connect_button);
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
    }
    public void joystickListener() {
        JoystickView joystick1 = findViewById(R.id.joystickMC);
        joystick1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if (joy1Angle != angle && joy1Strength != strength) {
                    joy1Angle = angle;
                    joy1Strength = strength;
                    int yStrength = (int) Math.round(strength * Math.cos(Math.toRadians(angle)));
                    int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("JX1 " + xStrength + " JY1 " + yStrength);
                    }
                    //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        private SensorEventListener gravitySensorEventListener = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent event) {
            TextView text1 =  findViewById(R.id.textView);
            TextView text2 =  findViewById(R.id.textView2);
            TextView text3 =  findViewById(R.id.textView3);
            text1.setText(String.valueOf(event.values[0]));
            text2.setText(String.valueOf(event.values[1]));
            text3.setText(String.valueOf(event.values[2]));
            int xStrength =  (int)Math.round(-25 * event.values[1]);
            int yStrength =  (int)Math.round(-25 * event.values[0]);
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
            mTcpClient.run(getApplicationContext());
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            System.out.println("Response: "+values[0]);
        }
    }
}
