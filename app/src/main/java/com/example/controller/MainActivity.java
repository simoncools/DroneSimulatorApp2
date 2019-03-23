package com.example.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    Context ctx;
    TcpClient mTcpClient;
    int joy1Angle,joy1Strength,joy2Angle,joy2Strength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joystickListener();
        buttonListener();
        ctx = getApplicationContext();

    }



    public void buttonListener(){
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(v->{
            if(mTcpClient != null) {
                if (!mTcpClient.ismRun()) {
                    new ConnectTask().execute("");
                } else {
                    mTcpClient.stopClient();
                }
            }else {
                new ConnectTask().execute("");
            }

        });

        final Button movementControlButton = findViewById(R.id.MC_button);
        movementControlButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MovementControlActivity.class);
            if(mTcpClient!=null) mTcpClient.stopClient();
           // intent.putExtra("tcpClient", mTcpClient);
            startActivity(intent);
        });

        final Button helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HelpActivity.class);
            if(mTcpClient!=null) mTcpClient.stopClient();
            // intent.putExtra("tcpClient", mTcpClient);
            startActivity(intent);
        });


    }


    public void joystickListener(){
        JoystickView joystick1 = findViewById(R.id.joystick1);
        joystick1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if(joy1Angle!=angle && joy1Strength!=strength){
                    joy1Angle=angle;
                    joy1Strength= strength;
                TextView joystickText = findViewById(R.id.joystick1text);
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                joystickText.setText("x"+ xStrength + " y" + yStrength);
                if(mTcpClient != null) {
                    mTcpClient.sendMessage("JX1 " + xStrength + " JY1 " + yStrength);
                }
              //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }}
        });

        JoystickView joystick2 = findViewById(R.id.joystick2);
        joystick2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if(joy2Angle!=angle && joy2Strength!=strength){
                    joy2Angle=angle;
                    joy2Strength= strength;
                    TextView joystickText = findViewById(R.id.joystick2text);
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                joystickText.setText("x"+ xStrength + " y" + yStrength);
                if(mTcpClient != null) {
                    mTcpClient.sendMessage("JX2 " + xStrength + " JY2 " + yStrength);
                }
              //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }}
        });
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            mTcpClient = new TcpClient(msg-> {
                    publishProgress(msg);
            });
            mTcpClient.run(ctx);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            System.out.println("Response: "+values[0]);
        }

    }
}
