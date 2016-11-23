package com.hiscinema.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    static final int PORT = 5000;

    public static void main(String args[]) throws IOException {
    	
    	
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server Listening on port "+PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
       
        try {
        	while (true) {
        		socket = serverSocket.accept();
        		// new thread for a client
        		new WebServerThread(socket).start();
        	}
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        } finally{
        	serverSocket.close();
        }
    }
}
