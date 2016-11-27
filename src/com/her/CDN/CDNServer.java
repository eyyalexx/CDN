package com.her.CDN;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.hiscinema.webserver.*;

public class CDNServer {

    static final int PORT = 5001;

    public static void main(String args[]) throws IOException {
    	
    	
        ServerSocket serverSocket = null;
        Socket socket = null;
        
        FileSystem fs = new FileSystem("public2");

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
        		new WebServerThread(socket, fs).start();
        	}
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        } finally{
        	serverSocket.close();
        }
    }
}
