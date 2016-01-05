package com.asian.billmanager.ws.json;

/*
 * DeleteBillResponse
 * 
 * Created: 05-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class DeleteBillResponse {
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
	public DeleteBillResponse(boolean result) {
		this.result = result;
		this.message = "";
	}
	
	public DeleteBillResponse(boolean result, String msg) {
		this.result = result;
		this.message = msg;
	}
	
	public DeleteBillResponse(boolean result, String msg, int billId) {
		this(result,msg);
		this.billId = billId;
	}
	
	public static DeleteBillResponse getSuccessResponse() {
		return new DeleteBillResponse(true);
	}
	
	public static DeleteBillResponse getSuccessResponseWithMessage(String msg) {
		return new DeleteBillResponse(true,msg);
	}
	
	public static DeleteBillResponse getSuccessResponseWithMessageAndBillId(String msg, int billId) {
		return new DeleteBillResponse(true,msg,billId);
	}
	
	public static DeleteBillResponse getFailureResponse() {
		return new DeleteBillResponse(false);
	}
	
	public static DeleteBillResponse getFailureResponseWithMessage(String msg) {
		return new DeleteBillResponse(false,msg);
	}
}
