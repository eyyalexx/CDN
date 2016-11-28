package com.client.browser;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class applicationClient {

	private static final String LDNSIP = "localhost";
	private static final String WEBSERVERIP = "localhost";
	private static final int WEBPORT = 5000;
	private static final int LDNSPORT = 5003;
	
	
	
	//TCP connection to WebServer
	private static String getData(String webServIP, int webServPort, String url) {

        String data = "";
        
        try {

           
           //Send the request to Webserver.
           Socket socket = new Socket(webServIP, webServPort);
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
            	
            	//parse the content length
            	String[] options = str.split(": ");
            	if(options[0].equals("Content-Length")){
            		dataLength = Integer.parseInt(options[1]);
            	}
            }
            

            int dataread = 0;
            
            while(dataread<dataLength){
            	//read the data sent
            	str = in.readLine();
            	data+= str+"\n";
            	dataread = data.length();
            }
            
            socket.close();

        } catch (UnknownHostException e1) {

        	System.err.println("Don't know about host " + webServIP);
            System.exit(1);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "+ webServIP);
            System.exit(1);
        }
        
        return data;
		
	}
	
	
	//UDP conncetion to local DNS
	public static String getIP(String lDNSIP, int lDNSPort, String addressToQeuery){
        
        try
        {
            DatagramSocket socket = new DatagramSocket();
            
            
            InetAddress lDNSAddress = InetAddress.getByName(lDNSIP);
            
            //create DNS query to send
            String s = addressToQeuery+" A" ;
            
            byte[] b = s.getBytes();
            
            DatagramPacket  dp = new DatagramPacket(b , b.length, lDNSAddress, lDNSPort);
            socket.send(dp);
            
            
            //now receive reply
            //buffer to receive incoming data
            byte[] buffer = new byte[256];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            
            //parse the reply for he ip address
            byte[] data = reply.getData();
            s = new String(data, 0, reply.getLength());
            
            
            
            socket.close();
            
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
 
		
		return "localhost";
	}
	
	//Main
	public static void main(String[] args) throws MalformedURLException {
		
		String urlToQueryWebServer = "GET / HTTP/1.1";
		
		//query the web server
		String data = getData(WEBSERVERIP, WEBPORT, urlToQueryWebServer);
	
		//parse the results for the links
		String[] options = data.split("\n");
		for(int i = 0; i < options.length; i++){
			System.out.println("Link " + (i+1) + ": " +options[i]);
		}
		
		//allow the use to pick a link to go to 
	    Scanner in = new Scanner(System.in);
		System.out.print("Select your link(1,2,3 or 4): ");
		int userInput = in.nextInt();
		
		in.close();
		
		URL urlSelected = new URL(options[userInput-1]); //offset because links starts from 1
		
		//parse the host name from the url
		String hostFromURL = urlSelected.getHost();
		
		String ipOfHost = getIP(LDNSIP, LDNSPORT, hostFromURL);
		
		
		
		
	}

}
