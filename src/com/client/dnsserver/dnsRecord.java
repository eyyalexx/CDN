package com.client.dnsserver;

public class dnsRecord {

	private String name;
	private int val;
	private String type;
	
	//default constructor
	public dnsRecord(){
		name = "";
		val = 0;
		type = "";
	}
	
	public dnsRecord(String name, int value, String tpye){
		this.name = name;
		this.val = value;
		this.type = type;
	}
	
	
	//get methods
	public String getName(){
		return(this.name);
	}
	
	public int getVal(){
		return(this.val);
	}
	
	public String getType(){
		return(this.type);
	}
	
}
