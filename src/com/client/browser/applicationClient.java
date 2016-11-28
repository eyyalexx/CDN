package com.client.browser;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class applicationClient {

	//private static final String hostadress = "localhost";
	private static final String hostadress = "localhost";
	private static final int portNumber = 5000;
	
	//TCP connection to WebServer
	private static String getData(String ip, int port, String url) {

        String data = "";
        
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
            //System.out.println(dataLength);
            
            
            //

            int dataread = 0;
            
            while(dataread<dataLength){
            	//read the data sent
            	str = in.readLine();
            	data+= str+"\n";
            	dataread = data.length();
            }
            //System.out.println(data);
            
            socket.close();

        } catch (UnknownHostException e1) {

        	System.err.println("Don't know about host " + hostadress);
            System.exit(1);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostadress);
            System.exit(1);
        }
        
        return data;
		
	}
	
	//UDP conncetion to local DNS
	public static void getIP(){			//Put (String URL) for argument later.
        DatagramSocket sock = null;
        int port = 5000;
        String s;
         
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
         
        try
        {
            sock = new DatagramSocket();
             
            InetAddress host = InetAddress.getByName("localhost");
             
            while(true)
            {
                //take input and send the packet
                System.out.println("Enter message to send : ");
                s = (String)cin.readLine();
                byte[] b = s.getBytes();
                 
                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                sock.send(dp);
                 
                //now receive reply
                //buffer to receive incoming data
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                sock.receive(reply);
                 
                byte[] data = reply.getData();
                s = new String(data, 0, reply.getLength());
                 
                //echo the details of incoming data - client ip : client port - client message
                System.out.println(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
 
		
		//return "Hello";
	}
	
	//Main
	public static void main(String[] args) {
		
		String url = "GET / HTTP/1.1";
		String userInput;
		
		String data = getData(hostadress, portNumber, url);
	
		String[] options = data.split("\n");
		
		for(int i = 0; i < options.length; i++){
			System.out.println("Link " + (i+1) + ": " +options[i]);
		}
		
	    Scanner in = new Scanner(System.in);
		System.out.print("Select your link(1,2,3 or 4): ");
		userInput = in.nextLine();
		System.out.println("You selected: " + userInput + ", " + options[Integer.parseInt(userInput)]);
		
		getIP();
		
	}

}
