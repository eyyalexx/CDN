package com.hiscinema.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebServerThread extends Thread {
    
	protected Socket socket;
    private SendHTTPMessage http;
    private FileSystem fs;
    
    public WebServerThread(Socket clientSocket, FileSystem fs) {
        this.socket = clientSocket;
        this.fs = fs;
    }

    public void run() {
    	System.out.println("New Client connected");
        BufferedReader in = null;
        PrintWriter out = null;
        
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            
            //to reply to the http message sent
            http = new SendHTTPMessage(out);
            
            // read the data sent. We stop reading once a blank line is hit
            String str = ".";
            String request = "";
            while (!str.equals("")){
              str = in.readLine();
              request += str+"\n";
            }
            
            System.out.println(request);
            
            parseHTTP(request);
            
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private void parseHTTP(String req){
    	
    	boolean badRequest = true;
    	
    	//lines of the http req
    	String[] lines = req.split("\n");
    	
    	for(int i =0; i< lines.length; i++){
    		//if the line is a GET request
    		if(lines[i].length() > 2 && lines[i].substring(0, 3).equals("GET")){
    			badRequest = false;
    			
    			//split the get request 
    			String[] get = lines[i].split(" ");
    			
    			//incorrect version of http
    			if(!get[2].equals(SendHTTPMessage.HTTPVERSION)){
    				http.sendMessage(505);
    			}else{
    				//use the url from the request to send the requested file
        			fs.printFile(get[1], http);
    			}
    			
    		}
    	}
    	//the request is invalid
    	if(badRequest){
    		http.sendMessage(400);
    	}
    }
    
}
