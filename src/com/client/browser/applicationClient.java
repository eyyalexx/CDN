package com.client.browser;

import java.io.*;
import java.net.*;

public class applicationClient {

	private static final String hostadress = "localhost";
	private static final int portNumber = 5000;

	public static void main(String[] args) throws IOException {

		String url = "GET /index.html HTTP/1.1";

        try (
           Socket socket = new Socket(hostadress, portNumber);

           PrintWriter out =
           		new PrintWriter(socket.getOutputStream(), true);

		   BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        ) {

        	//Write to socket
        	out.println(url);


        } catch (UnknownHostException e1) {

        	System.err.println("Don't know about host " + hostadress);
            System.exit(1);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostadress);
            System.exit(1);
        } 

	}

}
