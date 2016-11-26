//package com.client.browser;

import java.awt.*;
import java.awt.Dimension;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class applicationClient {

	//private static final String hostadress = "localhost";
	private static final String hostadress = "10.16.112.207";
	private static final int portNumber = 5000;
	
	
/*
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
*/

	public static void main(String[] args) throws IOException {
		
/*
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
*/
		
		String url = "GET /index.html HTTP/1.1";

        try {


           Socket socket = new Socket(hostadress, portNumber);


           PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		   BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         

        	//Write to socket
        	out.println(url);
        	//out.println("\r\n");
        	out.println("exit");
        	out.flush();
        	//out.close();
        	
        	
        	String inputLine;
			StringBuffer response = new StringBuffer();

			
			
			//System.out.println("Hi");
			while ((inputLine = in.readLine()) != null) {
				//System.out.print("Hello");
				response.append(inputLine);
			}
			//in.close();
			
			
			
			System.out.println(response.toString());
			//System.out.println("Hello World!");


        } catch (UnknownHostException e1) {

        	System.err.println("Don't know about host " + hostadress);
            System.exit(1);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostadress);
            System.exit(1);
        } 

	}

}
