package com.asian.billmanager.ws.json;

public class ServiceResponse {
	private int code = 0;
	private String msg = null;
	
	public ServiceResponse() {
		
	}
	
	public ServiceResponse (int statusCode, String message) {
		this.code = statusCode;
		this.msg = message;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.msg;
	}
}
