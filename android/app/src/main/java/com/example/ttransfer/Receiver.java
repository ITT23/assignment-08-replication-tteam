package com.example.ttransfer;
// https://stackoverflow.com/questions/59054867/reading-data-from-socket-using-asynctask-in-android-studio
/*
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver extends AsyncTask<String, String, String>{
    private Base64DataCallback callback;

    public Receiver(Base64DataCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... voids) {
        String host = voids[0];
        int port = Integer.parseInt(voids[1]);
        try {
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String base64Data = reader.readLine();
            publishProgress(base64Data);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (callback != null) {
            String base64Data = values[0];
            callback.onDataReceived(base64Data);
        }
    }
}*/

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver extends AsyncTask<String, String, Void> {

    @Override
    protected Void doInBackground(String... voids) {
        String SERVER_HOST = voids[0];
        int SERVER_PORT = Integer.parseInt(voids[1]);
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = reader.readLine()) != null) {
                publishProgress(message);
            }

            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... messages) {
        for (String message : messages) {
            // Received a message from the server
            Log.d("YUUUUUUUUUUUUUUU", "Received message from server: " + message);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // The connection to the server has closed
        Log.d("YUUUUUUUUUUUUUUU", "Connection to server closed");
    }
}