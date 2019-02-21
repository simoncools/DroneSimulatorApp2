package com.example.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    TCPConnection connection = new TCPConnection("192.168.1.50", 1234);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joystickListener();
        buttonListener();
    }

    public void buttonListener(){
        //connection button
        final Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if (!connection.isConnected()) {
                    String resp = connection.startConnection();
                    if ("connection succes" == resp) {
                        connectButton.setText("disconnect");
                    }
                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                } else {
                    String resp = connection.stopConnection();
                    if ("disconnect succes" == resp) {
                        connectButton.setText("connect");
                    }
                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void joystickListener(){
        JoystickView joystick1 = findViewById(R.id.joystick1);
        joystick1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                TextView joystickText = findViewById(R.id.joystick1text);
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                joystickText.setText("x"+ xStrength + " y" + yStrength);
                String resp = connection.sendMessage("x"+ xStrength + " y" + yStrength);
              //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }
        });

        JoystickView joystick2 = findViewById(R.id.joystick2);
        joystick2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                TextView joystickText = findViewById(R.id.joystick2text);
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                joystickText.setText("x"+ xStrength + " y" + yStrength);
                String resp = connection.sendMessage("x"+ xStrength + " y" + yStrength);
              //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
