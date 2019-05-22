package com.example.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        EditText IP = findViewById(R.id.serverIPeditText);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        IP.setText(pref.getString("serverIP","192.168.0.10"));
        final Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("serverIP",IP.getText()+"");
            editor.commit();
            this.finish();
        });

    }

}
