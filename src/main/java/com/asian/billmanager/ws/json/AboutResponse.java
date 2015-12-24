package com.asian.billmanager.ws.json;

/*
 * AboutResponse
 * 
 * Created: 24-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AboutResponse {
	String appName;
	String appVersion;
	String appBuildTime;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getAppBuildTime() {
		return appBuildTime;
	}
	public void setAppBuildTime(String appBuildTime) {
		this.appBuildTime = appBuildTime;
	}
}
