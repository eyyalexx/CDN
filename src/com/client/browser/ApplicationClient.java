package com.client.browser;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.helper.classes.Addresses;
import com.helper.classes.DnsQuery;
import com.helper.classes.Packet;


public class ApplicationClient {

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
        	
        	System.out.println("Contacting webserver at: "+ webServIP+":"+webServPort +" with request: "+request);
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
        System.out.println("Received file from webserver: "+ packet.getFile());
        
        return packet;
	}
	
	
	//UDP conncetion to local DNS
	public static String getIP(String lDNSIP, int lDNSPort, String addressToQeuery){
        
		DnsQuery dnsReply = null;
        try
        {
            DatagramSocket socket = new DatagramSocket();
            
            InetAddress lDNSAddress = InetAddress.getByName(lDNSIP);
           
            String q = addressToQeuery+";V" ;
            //TODO: Use the DNSQuery class to encode the question
            DnsQuery queryToSend = new DnsQuery("6", 0, q, "");
            
            byte[] msgToSend = queryToSend.getQuery().getBytes();
           
            System.out.println("Contacting Local DNS server with query: "+queryToSend.getQuery());
            
            DatagramPacket  dp = new DatagramPacket(msgToSend, msgToSend.length, lDNSAddress, lDNSPort);
            socket.send(dp);
            
            
            //now receive reply
            //buffer to receive incoming data
            byte[] buffer = new byte[256];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            
            //parse the reply for the ip address
            byte[] data = reply.getData();
            String s = new String(data, 0, reply.getLength());
            
            //TODO: use the DnsQuery class to parse for the answer
            dnsReply = new DnsQuery(s);
            
            
            socket.close();
            
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
        
        String answer = dnsReply.getAnswer();
        String[] parse = answer.split(";");//val, type
        
		System.out.println("Recieved answer from local DNS: "+answer);
		
		return parse[0];
	}
	
	//Main
	public static void main(String[] args) throws IOException {
		
		String urlToQueryWebServer = "/index.html";
		
		//query the web server
		Packet p = getData(Addresses.WEBSERVERIP, Addresses.WEBSERVERPORT, urlToQueryWebServer);
	    
		String path = p.getFile();
		String data = readFile(path);
		
		//parse the packet data for the links
		String[] options = data.split("\n");
		for(int i = 0; i < options.length; i++){
			System.out.println("Link " + (i+1) + ": " +options[i]);
		}
		
		//allow the user to pick a link to go to 
	    Scanner in = new Scanner(System.in);
		System.out.print("Select your link(1,2,3 or 4): ");
		int userInput = in.nextInt();
		in.close();
		
		URL urlSelected = new URL(options[userInput-1]); //offset because links starts from 1
		
		//parse the host name from the url
		String hostFromURL = urlSelected.getHost();
		
		System.out.println(hostFromURL);
		
		String ipOfHost = getIP(Addresses.LOCALDNSIP, Addresses.LOCALDNSPORT, hostFromURL);
		
		System.out.println("IP: "+ ipOfHost);
		
		//query the CDN Server
		p = getData(ipOfHost, Addresses.CDNSERVERPORT, urlSelected.getFile());
	    
		path = p.getFile();
		
		playFile(path);
		
		
		
	}
	
	public static String readFile(String path) throws IOException{
		String data = "";
		BufferedReader br;
		String sCurrentLine;
		if(path != null){
			//read the file
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
				data +=sCurrentLine+"\n";
			}
		}
		
		return data;
	}
	
	public static void playFile(String path) throws IOException{
		
		if(System.getProperty("os.name").contains("Linux")){
			
			//LINUX PATH BELOW
			Runtime.getRuntime().exec("vlc "+path);
		}
	
		if(System.getProperty("os.name").contains("Windows")){
			//c/Users/Alex/Documents/GitHub/CDN/cache/localhost5001
			//WINDOWS PATH BELOW
			Runtime.getRuntime().exec("C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe "+path);
	
		}
	}

}
