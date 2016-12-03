package com.hiscinema.dnsserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.helper.classes.Addresses;
import com.helper.classes.DnsQuery;
import com.helper.classes.DnsRecord;

public class HisDNSServer {
	
	//given a host, find the dns with the ip
	public static DnsRecord findDNS(String host, ArrayList<DnsRecord> records){
		for(DnsRecord r : records){
			if(r.getName().equals(host)){
				if(r.getType().equals("A")){
					return r;
				}else if(r.getType().equals("R")){
					return r;
				}else{
					return findDNS(r.getVal() ,records);
				}
				
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		ArrayList<DnsRecord> records = new ArrayList<DnsRecord>(); 
		
		records.add(new DnsRecord("video.hiscinema.com", "herCDN.com", "R"));
		
		
		DatagramSocket sock = null;
        
        try
        {
            //1. creating a server socket, parameter is local port number
        	if(Addresses.ONEMACHINE){
        		sock = new DatagramSocket(Addresses.HISDNSPORT);
        	}else{
        		sock = new DatagramSocket(Addresses.ADNSPORT);
        	}
             
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
                DnsQuery queryRec = new DnsQuery(s);
                
                
                //the details of incoming data - client ip : client port - client message
                String addressOfSender = incoming.getAddress().getHostAddress();
                int portOfSender = incoming.getPort();
                
                InetAddress ipToQuery;
                int portToSendTo;
                byte[] dataToSend;
                
            	
            	String question = queryRec.getQuestion();
            	String[] hostAndType = question.split(";");
            	
            	String qHost = hostAndType[0];
            	String qType = hostAndType[1]; // should be V in most cases
           
            	DnsRecord dnsToQuery = findDNS(qHost, records);
            	
            	//prepare to send answer back
                ipToQuery = InetAddress.getByName(addressOfSender);
                portToSendTo = portOfSender;
                
                //(String id, int flag, String question, String answer)
                DnsQuery answerToSendBack = new DnsQuery(queryRec.getID(), 1, queryRec.getQuestion(), dnsToQuery.getVal()+";NS");
                
                
                dataToSend = answerToSendBack.getQuery().getBytes();
                
                //send data
                DatagramPacket  dp = new DatagramPacket(dataToSend , dataToSend.length, ipToQuery, portToSendTo);
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
