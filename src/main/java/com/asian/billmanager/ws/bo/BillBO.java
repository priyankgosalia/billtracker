package com.asian.billmanager.ws.bo;

import java.sql.Date;
import java.sql.Timestamp;

/*
 * BillBO
 * 
 * Business Object - Bill
 * 
 * Created: 25-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class BillBO {
	private int id;
	private int masterId;
	private String company;
	private Double amount;
	private String user;
	private String frequency;
	private String paymentMode;
	private String description;
	private String location;
	private String status;
	private int dueDay;
	private Date dueDate;
	private boolean deleted;
	private Timestamp creationDate;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BillBO [id=");
		builder.append(id);
		builder.append(", masterId=");
		builder.append(masterId);
		builder.append(", company=");
		builder.append(company);
		builder.append(", location =");
		builder.append(location);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", user=");
		builder.append(user);
		builder.append(", frequency=");
		builder.append(frequency);
		builder.append(", paymentMode=");
		builder.append(paymentMode);
		builder.append(", description=");
		builder.append(description);
		builder.append(", status=");
		builder.append(status);
		builder.append(", dueDay=");
		builder.append(dueDay);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + id;
		result = prime * result + masterId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BillBO)) {
			return false;
		}
		BillBO other = (BillBO) obj;
		if (company == null) {
			if (other.company != null) {
				return false;
			}
		} else if (!company.equals(other.company)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (masterId != other.masterId) {
			return false;
		}
		return true;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMasterId() {
		return masterId;
	}
	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getDueDay() {
		return dueDay;
	}
	public void setDueDay(int dueDay) {
		this.dueDay = dueDay;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public void setDeleted(boolean b) {
		this.deleted = b;
	}
	public boolean isDeleted() {
		return this.deleted;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
}
