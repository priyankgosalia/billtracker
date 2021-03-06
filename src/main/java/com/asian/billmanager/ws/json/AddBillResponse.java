package com.asian.billmanager.ws.json;

/*
 * AddBillResponse
 * 
 * Created: 01-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AddBillResponse {
	boolean result;
	String message;
	int billId;
	
	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
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
	public AddBillResponse(boolean result) {
		this.result = result;
		this.message = "";
	}
	
	public AddBillResponse(boolean result, String msg) {
		this.result = result;
		this.message = msg;
	}
	
	public AddBillResponse(boolean result, String msg, int billId) {
		this(result,msg);
		this.billId = billId;
	}
	
	public static AddBillResponse getSuccessResponse() {
		return new AddBillResponse(true);
	}
	
	public static AddBillResponse getSuccessResponseWithMessage(String msg) {
		return new AddBillResponse(true,msg);
	}
	
	public static AddBillResponse getSuccessResponseWithMessageAndBillId(String msg, int billId) {
		return new AddBillResponse(true,msg,billId);
	}
	
	public static AddBillResponse getFailureResponse() {
		return new AddBillResponse(false);
	}
	
	public static AddBillResponse getFailureResponseWithMessage(String msg) {
		return new AddBillResponse(false,msg);
	}
}
