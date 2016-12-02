package com.helper.classes;

public class TestDNSQuery {

	public static void main(String[] args) {
		DnsQuery dns = new DnsQuery("1111", 0, "hiscinema.com V", "");
		String a = dns.getQuery();
		System.out.println(a);
		
		DnsQuery parse = new DnsQuery(a);
		System.out.println(parse.getQuery());
		
	}

}
