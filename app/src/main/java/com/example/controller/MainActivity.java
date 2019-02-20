package com.example.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joystickListener();
    }

    public void joystickListener(){
        JoystickView joystick1 = (JoystickView) findViewById(R.id.joystick1);
        joystick1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                TextView joystickText = findViewById(R.id.joystick1text);
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                joystickText.setText("x"+ xStrength + " y" + yStrength);
            }
        });

        JoystickView joystick2 = (JoystickView) findViewById(R.id.joystick2);
        joystick2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                TextView joystickText = findViewById(R.id.joystick2text);
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                joystickText.setText("x"+ xStrength + " y" + yStrength);
            }
        });
    }


}
