package com.helper.classes;

public class DnsQuery {

	
	private Short identification;
	private Short flags; //0 for questions and 1 for response (state of query)
	private Short NumberOfQuestions;
	private Short NumberOfAnswers;
	private Short NumberOfAuthority;
	private Short NumberOfAdditional;
	private String questions;
	private String answers;
	private String authority;
	
	
	public DnsQuery(Short identification, Short flags, Short NumberOfQuestions, 
					Short NumberOfAnswers, Short NumberOfAuthority, Short NumberOfAdditional,
					String questions, String answers, String authority){
		
		this.identification = identification;
		this.flags = flags;
		this.NumberOfQuestions = NumberOfQuestions;
		
		//Query with/without answers
		if(NumberOfAnswers == 0){
			answers = "";
		} else {
			this.answers = answers;
		}
		
		this.NumberOfAnswers = NumberOfAnswers;
		this.NumberOfAuthority = NumberOfAuthority;
		this.NumberOfAdditional = NumberOfAdditional;
		this.questions = questions;
		this.authority = authority;
	}
	
	public String getQuery(){
		String query;
		

		
		
		return "";
	}
	
	public Short getID(){
		return this.identification;
	}
	
	public Short getFlags(){
		return this.flags;
	}
	
	public Short getNumberOfQuestions(){
		return this.NumberOfQuestions;
	}

	public Short getNumberOfAnswers(){
		return this.NumberOfAnswers;
	}
	
	public Short getNumberOfAuthority(){
		return this.NumberOfAuthority;
	}
	
	public Short getNumberOfAdditional(){
		return this.NumberOfAdditional;
	}
	
	public String getanswers(){
		return this.answers;
	}
	
	public String getquestions(){
		return this.questions;
	}
	
	public String getauthority(){
		return this.authority;
	}
}
