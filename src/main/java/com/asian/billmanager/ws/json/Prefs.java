package com.asian.billmanager.ws.json;

/*
 * Prefs
 * 
 * Created: 14-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class Prefs {
	String username;
	String firstName;
	String lastName;
	String password;
	String confirmPassword;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Prefs [username=");
		builder.append(username);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", confirmPassword=");
		builder.append(confirmPassword);
		builder.append("]");
		return builder.toString();
	}
}
