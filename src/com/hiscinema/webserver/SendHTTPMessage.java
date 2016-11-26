package com.hiscinema.webserver;

import java.io.PrintWriter;
import java.util.HashMap;

public class SendHTTPMessage {

	private PrintWriter out;
	private HashMap<Integer, String> httpCodes = new HashMap<Integer, String>();
	//private static final String crlf = "\r\n"; //carriage return and line-feed characters
	public static final String HTTPVERSION = "HTTP/1.1";
	
	public SendHTTPMessage(PrintWriter out){
		this.out = out;
		httpCodes.put(200, "OK");
		httpCodes.put(400, "Bad Request");
		httpCodes.put(404, "Not Found");
		httpCodes.put(505, "HTTP Version Not Supported");
	}
	
	public void sendMessage(int httpCode, String data){
		
		String codeMessage = httpCodes.get(httpCodes);
		out.println(HTTPVERSION+" "+httpCode+" "+codeMessage);//+crlf);
		out.println("Content-Length: "+data.length());
		out.println("");//out.println(crlf);
		out.println(data);
		out.flush();
	}
	
	public void sendMessage(int httpCode){
		String codeMessage = httpCodes.get(httpCode);
		
		String data = httpCode+" "+codeMessage;
		
		out.println(HTTPVERSION+" "+httpCode+" "+codeMessage);//+crlf);
		out.println("Content-Length: "+data.length());
		out.println("");//out.println(crlf);
		out.println(data);
		out.flush();
	}
	
}
