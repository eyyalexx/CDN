package com.hiscinema.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileSystem {

	static String dir = "public";
	final static String DEFAULT_FILE = "/index.html";
	
	
	/**
	 * 
	 * @param path the path to the file being requested
	 */
	public static void printFile(String path, PrintWriter out){
		BufferedReader br = null;

		if(path.equals("/")){
			path = DEFAULT_FILE;
		}
		
		
		try {
			
			File f = new File(dir+path);
			if(f.exists() && !f.isDirectory()){
				String sCurrentLine;
				
				br = new BufferedReader(new FileReader(dir+path));
	
				while ((sCurrentLine = br.readLine()) != null) {
					out.println(sCurrentLine);
				}
			}else{
				
				out.println("<h1>404 Not Found</h1>");
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		
		
		
		
		
	}
	
	
	
	
}
