package com.example.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class movementActivity extends AppCompatActivity {

    private Sensor gravitySensor;
    private SensorManager gravitySensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);

        gravitySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = gravitySensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gravitySensorManager.registerListener(gravitySensorEventListener, gravitySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener gravitySensorEventListener = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent event) {
            TextView text1 =  findViewById(R.id.textView);
            TextView text2 =  findViewById(R.id.textView2);
            TextView text3 =  findViewById(R.id.textView3);
            text1.setText(String.valueOf(event.values[0]));
            text2.setText(String.valueOf(event.values[1]));
            text3.setText(String.valueOf(event.values[3]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

}
