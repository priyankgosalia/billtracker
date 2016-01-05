package com.asian.billmanager.ws.json;

/*
 * DeleteBillRequest
 * 
 * Created: 05-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class DeleteBillRequest {
	int billId;
	boolean deleteRecur;
	
	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
	}
	public boolean isDeleteRecur() {
		return deleteRecur;
	}
	public void setDeleteRecur(boolean deleteRecur) {
		this.deleteRecur = deleteRecur;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeleteBillRequest [billId=");
		builder.append(billId);
		builder.append(", deleteRecur=");
		builder.append(deleteRecur);
		builder.append("]");
		return builder.toString();
	}
	
}