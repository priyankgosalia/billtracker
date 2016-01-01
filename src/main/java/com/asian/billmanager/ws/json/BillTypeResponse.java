package com.asian.billmanager.ws.json;

/*
 * BillTypeResponse
 * 
 * Created: 31-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class BillTypeResponse {
	private String billType;
	private String billTypeDesc;
	
	public BillTypeResponse(String type, String desc) {
		this.billType = type;
		this.billTypeDesc = desc;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billType == null) ? 0 : billType.hashCode());
		result = prime * result + ((billTypeDesc == null) ? 0 : billTypeDesc.hashCode());
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
		if (!(obj instanceof BillTypeResponse)) {
			return false;
		}
		BillTypeResponse other = (BillTypeResponse) obj;
		if (billType == null) {
			if (other.billType != null) {
				return false;
			}
		} else if (!billType.equals(other.billType)) {
			return false;
		}
		if (billTypeDesc == null) {
			if (other.billTypeDesc != null) {
				return false;
			}
		} else if (!billTypeDesc.equals(other.billTypeDesc)) {
			return false;
		}
		return true;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillTypeDesc() {
		return billTypeDesc;
	}
	public void setBillTypeDesc(String billTypeDesc) {
		this.billTypeDesc = billTypeDesc;
	}
}
