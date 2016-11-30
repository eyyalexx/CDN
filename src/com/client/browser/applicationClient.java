package com.client.browser;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class applicationClient {

	private static final String LDNSIP = "localhost";
	private static final String WEBSERVERIP = "localhost";
	private static final int WEBPORT = 5000;
	private static final int LDNSPORT = 5003;
	private static final String HTTPVERSION = "HTTP/1.1";
	private static final int MAX_PACKET_SIZE = 65535;
	
	public static boolean createDir(String fname){
		File theDir = new File(fname);
		boolean result = false;
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        
		    }
		}
		return result;	
	}
	
	
	//TCP connection, Get file from web server
	private static Packet getData(String webServIP, int webServPort, String url) {
        
        String request = "GET "+ url+" "+HTTPVERSION+"\n\n";
        
        Packet packet = new Packet();
        
        try {

			// Send the request to Webserver.
			Socket socket = new Socket(webServIP, webServPort);

			InputStream sockIn = socket.getInputStream();
			OutputStream sockOut = socket.getOutputStream();
			
			
			// write the http request
			sockOut.write(request.getBytes());
			
			byte[] bytes = new byte[MAX_PACKET_SIZE];
			//wait for reply
			
			int count;
			boolean headerRead = false;
			String header ="";
			byte[] data = null;
			
			
			String path = "cache/"+webServIP+webServPort;
			
			createDir(path);
			
			FileOutputStream out = new FileOutputStream(path+url);
			
			
	        while ((count = sockIn.read(bytes)) > 0) {
	        	//first packet
	        	if(!headerRead){
	        		String dataRead = new String(bytes, 0, count);
	        		//split the http message by lines
	        		String[] lines = dataRead.split("\r\n", -1);
	        		
	        		int i = 0;
	        		while(!lines[i].equals("")){
	        			header+=lines[i]+"\n";
	        			i++;
	        		}
	        		
	        		//the index of the data
	        		i++;
	        		if(i < lines.length){
	        			data = lines[i].getBytes();
	        			out.write(data);
	        		}
	        		
	        		//dont look for header after
	        		headerRead = true;
	        	}else{ //if only data
	        		data = bytes;
	        		out.write(data, 0, count);
	        	}
	        	
	        }
	        packet.addHeader(header);
	        if(data != null){
	        	packet.addFile(path+url);
	        }
	        
	        
	        out.close();
			
			
			//parse reply and the http header
			/*String str = ".";
			int dataLength = 0;
			
			String header ="";
			
			while (!str.equals("")) {
				str = br.readLine();
				header+= str+"\n";
				// parse the content length
				String[] options = str.split(": ");
				if (options[0].equals("Content-Length")) {
					dataLength = Integer.parseInt(options[1]);
				}
			}
			packet.addHeader(header);
			
			
			byte[] bytes = new byte[dataLength]; // 10 is extra buffer space 
			
			int count;	
			//read the data
			while((count = sockIn.read(bytes)) < 0){
				System.out.println(count);
			}
			
			
			packet.addData(bytes);
			*/
            //fos.close();
            sockOut.close();
            sockIn.close();
            socket.close();

        } catch (UnknownHostException e1) {

        	System.err.println("Don't know about host " + webServIP);
            System.exit(1);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return packet;
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
            
            //parse the reply for the ip address
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
	public static void main(String[] args) throws IOException {
		
		String urlToQueryWebServer = "/index.html";
		
		//query the web server
		Packet p = getData(WEBSERVERIP, WEBPORT, urlToQueryWebServer);
	    
		String path = p.getFile();
		String data = "";
		
		String sCurrentLine;
		BufferedReader br;
		if(path != null){
			//read the file
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
				data +=sCurrentLine+"\n";
			}
		}
		
		//parse the packet data for the links
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
		
		//String ipOfHost = getIP(LDNSIP, LDNSPORT, hostFromURL);
		
		
		
		
	}

}
