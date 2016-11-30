package com.hiscinema.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class SendHTTPMessage {

	private OutputStream os;
	
	private static final String DIRTOHTTPCODEFILES = "HTTPCodes";
	private static final String CRLF = "\r\n";
	private HashMap<Integer, String> httpCodes = new HashMap<Integer, String>();
	public static final String HTTPVERSION = "HTTP/1.1";
	private static final int MAX_PACKET_SIZE = 65535;
	
	public SendHTTPMessage(OutputStream os){
		this.os = os;
		
		httpCodes.put(200, "OK");
		httpCodes.put(400, "Bad Request");
		httpCodes.put(404, "Not Found");
		httpCodes.put(505, "HTTP Version Not Supported");
	}
	
	public void sendMessage(int httpCode, File f){
		
		//header
		String codeMessage = httpCodes.get(httpCode);
		String header= "";
		header += HTTPVERSION+" "+httpCode+" "+codeMessage+CRLF;
		header+="Content-Length: "+f.length() +CRLF;
		header+=CRLF;
		
		try {
			//write header
			os.write(header.getBytes());
			
			//the buffer
			byte[] bytes = new byte[(int)f.length()];
			FileInputStream fis = new FileInputStream(f);
			
			int count;
	        while ((count = fis.read(bytes)) > 0) {
	            os.write(bytes, 0, count);
	        }
	        
	        fis.close();
	        os.flush();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(int httpCode){
		String codeMessage = httpCodes.get(httpCode);
		
		File f = new File(DIRTOHTTPCODEFILES+"/"+httpCode+".html");
		sendMessage(httpCode, f);
	}
	
}
