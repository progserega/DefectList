package ru.drsk.progserega.defectlist;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Serega on 04.03.17.
 */
public class SyncService extends IntentService {
    public static final int SERVERPORT = 4444;
    // while this is true the server will run
    private boolean running = false;
    // used to send messages
    private PrintWriter bufferSender;
    private ServerSocket serverSocket;
    private Socket client;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
      // For our sample, we just sleep for 600 seconds.
      Log.i("SyncService:", "start");
      long endTime = System.currentTimeMillis() + 600*1000;
        /*
      while (System.currentTimeMillis() < endTime) {
          synchronized (this) {
              try {
                  Log.i("SyncService.onHandleIntent()", "main loop");
                  wait(5000);
              } catch (Exception e) {
              }
          }
      }
      */
        runTcpServer();

    }
    private void runTcpServer() {
        running = true;

        try {
            Log.i("runTcpServer()","S: Connecting...");

            // create a server socket. A server socket waits for requests to
            // come in over the network.
            serverSocket = new ServerSocket(SERVERPORT);

            // create client socket... the method accept() listens for a
            // connection to be made to this socket and accepts it.
            client = serverSocket.accept();

            Log.i("runTcpServer()","S: Receiving...");

            try {

                // sends the message to the client
                bufferSender = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream())), true);

                // read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        client.getInputStream()));

                // in this while we wait to receive messages from client (it's
                // an infinite loop)
                // this while it's like a listener for messages
                while (running) {

                    String message = null;
                    try {
                        message = in.readLine();
                    } catch (IOException e) {
                        System.out.println("Error reading message: "
                                + e.getMessage());
                    }
                    Log.i("runTcpServer():", "read from client:" + message);
/*
                    if (hasCommand(message)) {
                        continue;
                    }

                    if (message != null && messageListener != null) {
                        // call the method messageReceived from ServerBoard
                        // class
                        messageListener.messageReceived(message);
                    }
                    */
                }

            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
    }
}
