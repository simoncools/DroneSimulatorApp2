package com.example.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class helpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        TextView text1 = findViewById(R.id.textView4);
        TextView text2 = findViewById(R.id.textView5);
        TextView text3 = findViewById(R.id.textView6);

        text1.setText(pref.getString("serverIP",null));
        text2.setText(pref.getString("serverPort",null));
        text3.setText(pref.getBoolean("autoConnect",false)+"");


    }
}
