package com.helper.classes;
import java.io.IOException;
import java.lang.Runtime;
import java.io.*;

public class OpenVLC{

	
	
	public static void main(String[]args) throws IOException{
		
		if(System.getProperty("os.name").contains("Linux")){
			
			//LINUX PATH BELOW
			Runtime.getRuntime().exec("vlc /CDN-master/public2/SampleVideo_720x480_30mb.mp4");
		}
	
		if(System.getProperty("os.name").contains("Windows")){
			
			//WINDOWS PATH BELOW
			Runtime.getRuntime().exec("C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe C:\\Users\\Alex\\Documents\\GitHub\\CDN\\public2\\SampleVideo_720x480_30mb.mp4");
	
			
		}
	
		//System.out.println(System.getProperty("os.name"));
	}

}