package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPConnection {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip;
    private int port;


    public TCPConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String sendMessage(String msg) {
        String resp;
        try {
        out.println(msg);
            resp = in.readLine();
        }catch (Exception e){
            resp="failed, no connection";
        }
        return resp;
    }

    public String startConnection() {
        String resp ="succes";
        try{
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch (Exception e){
            resp="connecting failed to " + ip + ":" + port;
        }
        return resp;
    }

    public String stopConnection() {
        String resp = "connection succes";
        try{
            in.close();
            out.close();
            clientSocket.close();
            clientSocket=null;
        }catch (Exception e){
            resp="disconnecting failed";
        }
        return resp;
    }
    public boolean isConnected(){
        boolean resp = true;
        if(clientSocket==null)resp=false;
        return resp;
    }

}
