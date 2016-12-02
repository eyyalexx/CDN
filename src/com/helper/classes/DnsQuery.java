package com.helper.classes;

public class DnsQuery {

	private String identification;
	private int flag; //0 for questions and 1 for response (state of query)
	
	private String question;
	private String answer; 
	
	
	public DnsQuery(String id, int flag, String question, String answer){
		
		this.identification = id;
		this.flag = flag;
		this.question = question;
		this.answer = answer;
		
	}
	
	public DnsQuery(String data){
		
		String query = data;
		String delims = " ";
		String[] tokens = query.split(delims);
		
		if(tokens.length == 3){
			this.identification = tokens[0];
			this.flag = Integer.parseInt(tokens[1]);
			this.question = tokens[2];
			this.answer = tokens[3];
		}
		
		if(tokens.length == 2){
			this.identification = tokens[0];
			this.flag = Integer.parseInt(tokens[1]);
			this.question = tokens[2];
			this.answer = "";
		}
		
	}
	
	
	public boolean isQuestion(){
		if(this.flag == 0){
			return true;			//is a question
		} else if(this.flag == 1) {
			return false;			//is a response
		}
		
		return false;
	}
	

	
	public void addAnswer(String answer){
		this.answer = answer;
	}
	
	//get query only
	public String getQuery(){
		
		if(answer == ""){
			String query = this.identification + " " + this.flag + " " + this.question + " ";
			return query;
		} else {
			String query = this.identification + " " + this.flag + " " + this.question + " " + this.answer;
			return query;
		}
		
	}
	
	public String getID(){
		return this.identification;
	}
	
	public int getFlag(){
		return this.flag;
	}
	
	public String getAnswer(){
		return this.answer;
	}
	
	public String getQuestion(){
		return this.question;
	}
	
}
