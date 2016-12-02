package com.client.dnsserver;

public class DnsRecord {

	private String name;
	private String val;
	private String type;
	
	//default constructor
	public DnsRecord(){
		name = "";
		val = "";
		type = "";
	}
	
	public DnsRecord(String name, String value, String tpye){
		this.name = name;
		this.val = value;
		this.type = type;
	}
	
	
	//get methods
	public String getName(){
		return(this.name);
	}
	
	public String getVal(){
		return(this.val);
	}
	
	public String getType(){
		return(this.type);
	}
	
}
