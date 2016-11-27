package com.client.browser;

import java.io.*;
import java.net.*;

public class applicationClient {

	//private static final String hostadress = "localhost";
	private static final String hostadress = "localhost";
	private static final int portNumber = 5000;
	
	
	public static void main(String[] args) throws IOException {
		
		String url = "GET / HTTP/1.1";

        try {

           
           //Send the request to Webserver.
           Socket socket = new Socket(hostadress, portNumber);
           PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		   BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         

        	//write the http request
        	out.println(url);
        	out.println("");
        	out.flush();
        	
        	//the http header 
        	String str = ".";
        	int dataLength = 0;
        	
            while (!str.equals("")){
            	str = in.readLine();
            	
            	//in case content length is required in the future
            	String[] options = str.split(": ");
            	if(options[0].equals("Content-Length")){
            		dataLength = Integer.parseInt(options[1]);
            	}
            }
            //content length
            System.out.println(dataLength);
            
            //read the data sent
            String data = in.readLine();
            
            System.out.println(data);
            
            socket.close();

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
