package com.example.demo;

import java.util.Map;

public class Mail {

    private String from;
    private String to;
    private String content;
    private Map model;

    public Mail() {
    }

    public Mail(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
	public Map getModel() {
		// TODO Auto-generated method stub
		return model;
	}


    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

	public void setModel(Map model) {
		// TODO Auto-generated method stub
		this.model = model;
	}
}