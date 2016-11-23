package com.hiscinema.webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebServerThread extends Thread {
    protected Socket socket;

    public WebServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
    	System.out.println("New Client connected");
        BufferedReader in = null;
        PrintWriter out = null;
        
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            
            
            // read the data sent. We basically ignore it,
            // stop reading once a blank line is hit. This
            // blank line signals the end of the client HTTP
            // headers.
            String str = ".";
            String[] request = null;
            while (str != null && !str.equals("")){
              str = in.readLine();
              
              
              if(str.length() > 2 && str.substring(0, 3).equals("GET")){
            	  request = str.split(" ");
            	  
            	  
              }else{
            	  
              }
              
              
              System.out.println(str);
            }
            
            for(String a : request){
            	System.out.println("List= "+a);
            }
            
            if(request != null){
            	FileSystem.printFile(request[1], out);
            }else{
            	out.println("<h1>400 Bad Request</h1>");
            }
            
            
            out.flush();
            socket.close();
            
            
            
        } catch (IOException e) {
            out.println("<h1>500 Internal Server Error</h1>");
        }
        
        
      
    }
}
