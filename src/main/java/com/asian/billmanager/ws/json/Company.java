package com.asian.billmanager.ws.json;

/*
 * Company
 * 
 * Created: 19-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class Company {
	int id;
	String name;
	
	public Company(int aId, String aName) {
		this.id = aId;
		this.name = aName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ID = "+this.id+", Company Name = "+this.name;
	}
}
