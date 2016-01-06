package com.asian.billmanager.ws.json;

public class Reminder {
	int id;
	int masterReminderId;
	int daysRemaining;
	Bill bill;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMasterReminderId() {
		return masterReminderId;
	}

	public void setMasterReminderId(int masterReminderId) {
		this.masterReminderId = masterReminderId;
	}

	public int getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(int daysRemaining) {
		this.daysRemaining = daysRemaining;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}
}
