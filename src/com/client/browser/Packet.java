package com.client.browser;

public class Packet {

	private String header;
	private String file;
	
	public Packet(){
		header = "";
		file = null;
		
	}
	
	public void addHeader(String header){
		this.header = header;
	}
	
	public void addFile(String file){
		this.file = file;
	}
	
	public String getFile(){
		return file;
	}
	
	
	public String getHeader(){
		return header;
	}
}
