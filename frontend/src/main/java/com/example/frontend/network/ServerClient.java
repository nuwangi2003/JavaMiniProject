package com.example.frontend.network;

import java.io.*;
import java.net.Socket;

public class ServerClient {

    private static ServerClient instance;   // Singleton instance

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // private constructor (important for singleton)
    private ServerClient() {}

    public void connect() throws Exception {

        socket = new Socket("localhost", 5000);

        out = new PrintWriter(socket.getOutputStream(), true);

        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        System.out.println(in.readLine()); // server ready
    }

    public String sendRequest(String json) throws Exception {

        out.println(json);
        return in.readLine();
    }

    // Singleton access method
    public static synchronized ServerClient getInstance() {

        if (instance == null) {
            instance = new ServerClient();
        }

        return instance;
    }
}