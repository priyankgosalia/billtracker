package com.asian.billmanager.ws.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.asian.billmanager.version.Version;
import com.asian.billmanager.ws.json.AboutResponse;

/*
 * MetadataService
 * 
 * Created: 24-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
@Path("/metadata")
public class MetadataService extends Service {
	private static final Logger logger = LogManager.getLogger(MetadataService.class.getName());	
	private static final String SERVICE_NAME = "/metadata";

	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	@GET
	@Path("about")
	@Produces(MediaType.APPLICATION_JSON)
	public AboutResponse aboutApp() throws JSONException {
		AboutResponse response = new AboutResponse();
		response.setAppName(Version.APPNAME);
		response.setAppVersion(Version.VERSION);
		response.setAppBuildTime(Version.BUILDTIME);
		return response;
	}
}
