package com.asian.billmanager.ws.bo;

/*
 * ServiceHitsBO
 * 
 * Created: 02-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class ServiceHitsBO {
	private String name;
	private int hits;
	
	public ServiceHitsBO(String aName, int aHits) {
		this.name = aName;
		this.hits = aHits;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	
}
