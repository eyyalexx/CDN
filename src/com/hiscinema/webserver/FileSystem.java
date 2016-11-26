package com.hiscinema.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSystem {

	static String dir = "public";
	final static String DEFAULT_FILE = "/index.html";
	
	
	/**
	 * 
	 * @param path the path to the file being requested
	 */
	public static void printFile(String path, SendHTTPMessage http){
		BufferedReader br = null;

		if(path.equals("/")){
			path = DEFAULT_FILE;
		}
		
		
		try {
			
			File f = new File(dir+path);
			//checks if the file requested exists
			if(f.exists() && !f.isDirectory()){
				String sCurrentLine;
				String result= "";
				
				br = new BufferedReader(new FileReader(dir+path));
	
				//read file line by line
				while ((sCurrentLine = br.readLine()) != null) {
					result+= sCurrentLine+"\n";
				}
				//send http message with code 200
				http.sendMessage(200, result);
				
				br.close();
				
			}else{//if file does not exist
				http.sendMessage(404);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
