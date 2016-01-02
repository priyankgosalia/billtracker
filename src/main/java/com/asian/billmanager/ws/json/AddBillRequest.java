package com.asian.billmanager.ws.json;

/*
 * AddBillRequest
 * 
 * Created: 01-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AddBillRequest {
	char billType;
	int companyId;
	String dueDate;
	String location;
	String description;
	Double amount;
	String paymentMode;
	String userId;
	int paid;
	boolean recurrence;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddBillRequest [billType=");
		builder.append(billType);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append(", location=");
		builder.append(location);
		builder.append(", description=");
		builder.append(description);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", paymentMode=");
		builder.append(paymentMode);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", paid=");
		builder.append(paid);
		builder.append(", recurrence=");
		builder.append(recurrence);
		builder.append("]");
		return builder.toString();
	}

	public char getBillType() {
		return billType;
	}
	public void setBillType(char billType) {
		this.billType = billType;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPaid() {
		return paid;
	}
	public void setPaid(int paid) {
		this.paid = paid;
	}
	public boolean isRecurrence() {
		return recurrence;
	}
	public void setRecurrence(boolean recurrence) {
		this.recurrence = recurrence;
	}
}