package com.asian.billmanager.ws.bo;

public class ReminderBO extends BillBO {
	private int reminderId;
	private int masterReminderId;
	private int dueDays;
	private BillBO bill;
	
	public int getReminderId() {
		return reminderId;
	}
	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}
	public int getMasterReminderId() {
		return masterReminderId;
	}
	public void setMasterReminderId(int masterReminderId) {
		this.masterReminderId = masterReminderId;
	}
	public int getDueDays() {
		return dueDays;
	}
	public void setDueDays(int dueDays) {
		this.dueDays = dueDays;
	}
	public BillBO getBill() {
		return bill;
	}
	public void setBill(BillBO bill) {
		this.bill = bill;
	}
}
