package com.client.dnsserver;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.helper.classes.DnsRecord;

public class localdns {

	public static void main(String[] args) {
		
		ArrayList<DnsRecord> records = new ArrayList<DnsRecord>(); 
		
		records.add(new DnsRecord("video.hiscinema.com", "localhost", "A"));
		records.add(new DnsRecord());
		
		DatagramSocket sock = null;
        
        try
        {
            //1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(5000);
             
            //buffer to receive incoming data
            byte[] buffer = new byte[1024];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
             
            //2. Wait for an incoming data
            echo("Server socket created. Waiting for incoming data...");
             
            //communication loop
            while(true)
            {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                
                //incoming record
                String s = new String(data, 0, incoming.getLength());
                 
                //echo the details of incoming data - client ip : client port - client message
                //echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
                
                
                s = "OK : " + s;
                
                //send back to client
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
                
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
		

}
