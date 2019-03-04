package com.example.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class settingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText IP = findViewById(R.id.serverIPeditText);
        EditText port = findViewById(R.id.serverPortEditText);
        Switch autoConnect = findViewById(R.id.autoConnectSwitch);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        IP.setText(pref.getString("serverIP","192.168.43.145"));
        port.setText(pref.getString("serverPort","52832"));
        autoConnect.setChecked(pref.getBoolean("autoConnect", false));
        final Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("serverIP",IP.getText()+"");
            editor.putString("serverPort",port.getText()+"");
            editor.putBoolean("autoConnect",autoConnect.isChecked());
            editor.commit();
            this.finish();
        });
    }
}
