package com.example.aman.pccontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.aman.pccontrol.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class Client extends AppCompatActivity {
    EditText test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        test = (EditText) findViewById(R.id.test);
        ClientHTTPthread httpThread = new ClientHTTPthread() ;
        httpThread.start() ;

    }
    private class ClientHTTPthread extends Thread {
        static final int HttpPORT = 5000;

        @Override
        public void run() {

            try {
                final Socket socket = new Socket("192.168.43.207", 5000);
                if (socket != null) {
                    Log.d("Connection", "run: Success");
                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            test.setText("Test");
                        }
                    }));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private class ResponseThread extends Thread {
        Socket socket ;
        String msg ;
        int cnt ;

        ResponseThread(Socket socket, String msg, int cnt) {
            this.socket = socket ;
            this.msg = msg ;
            this.cnt = cnt ;
        }

        @Override
        public void run() {
            BufferedReader is ;
            PrintWriter os ;
            String request ;

            try {
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                request = is.readLine();
                System.out.println(request + " " + cnt) ;

                os = new PrintWriter(socket.getOutputStream(), true);

                String response = "<html><head></head>" + "<body>" + "<h1>" + msg + "</h1>" + "</body></html>";

                os.print("HTTP/1.0 200" + "\r\n");
                os.print("Content type: text/html" + "\r\n");
                os.print("Content length: " + response.length() + "\r\n");
                os.print("\r\n");
                os.print(response + "\r\n");
                os.flush();

                socket.close();
            } catch (IOException ie) {
                ie.printStackTrace() ;
            }

            return ;
        }
    }

    public void setText(){
        test.setText("set");
    }

}
