package com.client.dnsserver;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.helper.classes.Addresses;
import com.helper.classes.DnsQuery;
import com.helper.classes.DnsRecord;

public class Localdns {
		
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
		
		records.add(new DnsRecord("herCDN.com", "NSherCDN.com", "NS"));
		records.add(new DnsRecord("NSherCDN.com", Addresses.HERDNSIP, "A"));
		records.add(new DnsRecord("hiscinema.com", "NShiscinema.com", "NS"));
		records.add(new DnsRecord("NShiscinema.com", Addresses.HISDNSIP, "A"));
		
		ArrayList<QuestionAsked> questions = new ArrayList<QuestionAsked>();
		
		DatagramSocket sock = null;
        
        try
        {
            //1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(Addresses.LOCALDNSPORT);
             
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
                
                //if question: create new question
                if(queryRec.isQuestion()){
                	QuestionAsked q = new QuestionAsked(addressOfSender, portOfSender,queryRec);
                	questions.add(q);
                	
                	String question = queryRec.getQuestion();
                	String[] hostAndType = question.split(";");
                	
                	String qHost = hostAndType[0];
                	
                	String dotCom = qHost.substring(qHost.lastIndexOf("."));
                	String hostName = qHost.substring(0, qHost.lastIndexOf("."));
                	String hostName2 = hostName.substring(hostName.lastIndexOf(".")+1);
                	
                	String fHostName = hostName2+dotCom;
                	
                	String qType = hostAndType[1]; // should be V in most cases
                	
                	
                	
                	//send query based on records
                	DnsRecord whoToQuery = findDNS(fHostName, records);
                	
                	//prepare to send
                	
                    ipToQuery = InetAddress.getByName(whoToQuery.getVal());
                    
                    if(Addresses.ONEMACHINE){
                    	portToSendTo = Addresses.HISDNSPORT;
                    }else{
                    	portToSendTo = Addresses.ADNSPORT;
                    }
                    
                    dataToSend = queryRec.getQuery().getBytes();
                    
                }else{//if answer:
                	
                	String answer = queryRec.getAnswer();
                	String[] valType = answer.split(";"); // value, type
                	if(valType[1].equals("NS")){//type == NS
                		//send query to the redirect link
                		
                		DnsRecord dnsToQuery = findDNS(valType[0], records);
                		
                		ipToQuery = InetAddress.getByName(dnsToQuery.getVal());
                		
                		if(Addresses.ONEMACHINE){
                			portToSendTo = Addresses.HERDNSPORT;
                		}else{
                			portToSendTo = Addresses.ADNSPORT;
                		}
                		//(String id, int flag, String question, String answer)
                		DnsQuery toSend = new DnsQuery(queryRec.getID(), 0, valType[0]+";A", "");
                		
                		dataToSend = toSend.getQuery().getBytes();
                		
                	}else{//type == A
                		//send answer back to the question and remove from arrayList
                		
                		QuestionAsked qToSendBackTo= null;
                		
                		//find the question with the same id
                		for(QuestionAsked q : questions){
                    		if(q.getQuery().getID().equals(queryRec.getID())){//if ids are equals
                    			qToSendBackTo = q;
                    			questions.remove(q);
                    			break;
                    		}
                    	}
                		
                		ipToQuery = InetAddress.getByName(qToSendBackTo.getIpOfSender());
                		portToSendTo = qToSendBackTo.getPortOfSender();
                		
                		dataToSend = queryRec.getQuery().getBytes();
                	}
                	
                }
                
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
