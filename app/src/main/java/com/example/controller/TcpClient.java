package com.example.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {

    public static final String TAG = TcpClient.class.getSimpleName();
    public static final String SERVER_IP = "192.168.43.145"; //server IP address
    public static final int SERVER_PORT = 52832;
    private String mServerMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;

    public static String getServerIp() {
        return SERVER_IP;
    }

    public static int getServerPort() {
        return SERVER_PORT;
    }

    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    Log.d(TAG, "Sending: " + message);
                    mBufferOut.println(message);
                    mBufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stopClient() {
        mRun = false;
        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }
        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run(Context ctx) {
        mRun = true;
        try {
            SharedPreferences pref = ctx.getSharedPreferences("MyPref", 0); // 0 - for private mode
            InetAddress serverAddr = InetAddress.getByName(pref.getString("serverIP",SERVER_IP));
            System.out.println("Connecting...");
            Socket socket = new Socket(serverAddr,52832);
            try {
                System.out.println("Connected");
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(mServerMessage);
                    }
                }
            } catch (Exception e) {
               // System.out.println("Error");
            } finally {
                socket.close();
                System.out.println("Disconnected");
            }

        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public boolean ismRun() {
        return mRun;
    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}