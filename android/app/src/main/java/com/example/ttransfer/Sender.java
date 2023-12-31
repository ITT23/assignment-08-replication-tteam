package com.example.ttransfer;
// https://www.youtube.com/watch?v=29y4X65ZUwE

import android.os.AsyncTask;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends AsyncTask<String, Void, Void> {

    Socket s;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {
        String base64 = voids[0];
        String host = voids[1];
        int port = Integer.parseInt(voids[2]);
        try {
            s = new Socket(host, port);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(base64);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
