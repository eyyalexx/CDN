package com.client.dnsserver;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.helper.classes.DnsQuery;
import com.helper.classes.DnsRecord;

public class Localdns {

	private static final String IPher = "";
	private static final String IPhis = "";
	private static final int ADNSPORT = 5005; 
	
	
	//given a host, find the dns with the ip
	public static DnsRecord findDNS(String host, ArrayList<DnsRecord> records){
		for(DnsRecord r : records){
			if(r.getName().equals(host)){
				if(r.getType().equals("A")){
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
		
		records.add(new DnsRecord("herCDN.com", "NSherCDN.com", "NS"));
		records.add(new DnsRecord("NSherCDN.com", IPher, "A"));
		records.add(new DnsRecord("hiscinema.com", "NShiscinema.com", "NS"));
		records.add(new DnsRecord("NShiscinema.com", IPhis, "A"));
		
		ArrayList<QuestionAsked> questions = new ArrayList<QuestionAsked>();
		
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
                DnsQuery queryRec = new DnsQuery(s);
                
                
                //the details of incoming data - client ip : client port - client message
                String addressOfSender = incoming.getAddress().getHostAddress();
                int portOfSender = incoming.getPort();
                
                //if question: create new question
                if(queryRec.isQuestion()){
                	QuestionAsked q = new QuestionAsked(addressOfSender, portOfSender,queryRec);
                	questions.add(q);
                	
                	String question = queryRec.getQuestion();
                	String[] hostAndType = question.split(" ");
                	
                	String qHost = hostAndType[0];
                	String qType = hostAndType[1]; // should be V in most cases
                	
                	
                	//send query based on records
                	DnsRecord whoToQuery = findDNS(qHost, records);
                	
                	//prepare to send
                	
                    InetAddress ipToQuery = InetAddress.getByName(whoToQuery.getVal());
                    
                 
                    byte[] b = queryRec.getQuery().getBytes();
                    
                    DatagramPacket  dp = new DatagramPacket(b , b.length, ipToQuery, ADNSPORT);
                    sock.send(dp);
                	
                }else{//if answer:
                	
                	
                	
                }
                
                /*
                s = "OK : " + s;
                
                //send back to client
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
                */
                
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
