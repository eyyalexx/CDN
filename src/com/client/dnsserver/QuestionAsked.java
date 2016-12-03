package com.client.dnsserver;

import com.helper.classes.DnsQuery;

public class QuestionAsked {
	
	private String ipOfSender;
	private int portOfSender;
	private DnsQuery query;
	
	public QuestionAsked(String ipOfSender, int portOfSender, DnsQuery question){
		this.ipOfSender = ipOfSender;
		this.portOfSender = portOfSender;
		this.query = question;
	}

	public String getIpOfSender() {
		return ipOfSender;
	}

	public int getPortOfSender() {
		return portOfSender;
	}

	public DnsQuery getQuery() {
		return query;
	}


}
