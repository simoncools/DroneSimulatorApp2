package com.example.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    Context ctx;
    Button connectButton;
    TcpClient mTcpClient;
    int joy1xStrength, joy1yStrength, joy2xStrength, joy2yStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        joystickListener();
        buttonListener();
        locationsetup();

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



    public void buttonListener(){
        connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(v->{
            if(mTcpClient != null) {
                if (!mTcpClient.ismRun()) {
                    Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                    new ConnectTask().execute("");
                } else {
                    mTcpClient.stopClient();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                new ConnectTask().execute("");
            }


        });

        final Button movementControlButton = findViewById(R.id.MC_button);
        movementControlButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MotionControlActivity.class);
            if(mTcpClient!=null) mTcpClient.stopClient();
            startActivity(intent);
        });

        final Button helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HelpActivity.class);
            if(mTcpClient!=null) mTcpClient.stopClient();
            startActivity(intent);
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


    public void joystickListener(){
       /* JoystickView joystick1 = findViewById(R.id.joystick1);
        joystick1.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                if(joy1xStrength!=xStrength && joy1yStrength!=yStrength){
                    joy1xStrength=xStrength;
                    joy1yStrength= yStrength;
                if(mTcpClient != null) {
                    mTcpClient.sendMessage("JX1 " + xStrength + " JY1 " + yStrength);
                }
              //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }}
        },5);*/

        JoystickView joystick2 = findViewById(R.id.joystick2);
        joystick2.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {

                int yStrength = (int)  Math.round(strength * Math.cos(Math.toRadians(angle)));
                int xStrength = (int) Math.round(strength * Math.sin(Math.toRadians(angle)));
                if(joy2xStrength!=xStrength || joy2yStrength!=yStrength){
                    joy2xStrength=xStrength;
                    joy2yStrength= yStrength;
                if(mTcpClient != null) {
                    mTcpClient.sendMessage("JX2 " + xStrength + " JY2 " + yStrength);
                }
              //  if(resp!=null) Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }}
        },5);
    }
    public void locationsetup(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                System.out.println("location test: lat"+latitude+" long"+longitude);
                TextView text = findViewById(R.id.textView1);
                text.setText("lat"+latitude+" long"+longitude);
                if(mTcpClient != null) {
                    mTcpClient.sendMessage("gps "+latitude+" "+longitude);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //  public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults);
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("location no permission");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            mTcpClient = new TcpClient(msg-> {
                    publishProgress(msg);
            });
            mTcpClient.run(ctx,connectButton);
           // Toast.makeText(getApplicationContext(), "rip", Toast.LENGTH_SHORT).show();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            System.out.println("Response: "+values[0]);
        }

    }
}
