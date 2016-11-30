package com.hiscinema.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class FileSystem {

	private String dir;
	final static String DEFAULT_FILE = "/index.html";
	
	
	public FileSystem(String dir){
		this.dir = dir;
	}
	
	public String getDir(){
		return dir;
	}
	
	/**
	 * 
	 * @param path the path to the file being requested
	 */
	public void printFile(String path, SendHTTPMessage http){
		

		if(path.equals("/")){
			path = DEFAULT_FILE;
		}
		
		
		File f = new File(dir+path);
		//checks if the file requested exists
		if(f.exists() && !f.isDirectory()){
			

			//send http message with code 200
			http.sendMessage(200, f);
			
		}else{//if file does not exist
			http.sendMessage(404);
		}
	}
	
}
