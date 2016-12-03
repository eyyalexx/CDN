package com.her.CDN;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.helper.classes.Addresses;
import com.hiscinema.webserver.*;

public class CDNServer {

    public static void main(String args[]) throws IOException {
    	
    	
        ServerSocket serverSocket = null;
        Socket socket = null;
        
        FileSystem fs = new FileSystem("public2");

        try {
            serverSocket = new ServerSocket(Addresses.CDNSERVERPORT);
            System.out.println("Server Listening on port "+serverSocket.getLocalPort());
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
