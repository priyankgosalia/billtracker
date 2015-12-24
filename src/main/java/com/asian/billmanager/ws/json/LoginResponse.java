package com.asian.billmanager.ws.json;

/*
 * LoginResponse
 * 
 * Created: 12-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class LoginResponse {
	boolean result;
	String message;
	String userFirstName;
	
	public LoginResponse(boolean result) {
		this.result = result;
		this.message = "";
	}
	
	public LoginResponse(boolean result, String msg) {
		this.result = result;
		this.message = msg;
	}
	
	public static LoginResponse getSuccessResponse() {
		return new LoginResponse(true);
	}
	
	public static LoginResponse getSuccessResponseWithMessage(String msg) {
		return new LoginResponse(true,msg);
	}
	
	public static LoginResponse getFailureResponse() {
		return new LoginResponse(false);
	}
	
	public static LoginResponse getFailureResponseWithMessage(String msg) {
		return new LoginResponse(false,msg);
	}
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
}
