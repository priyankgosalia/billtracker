package com.asian.billmanager.ws.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.apache.logging.log4j.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import com.asian.billmanager.ws.bo.UserBO;
import com.asian.billmanager.ws.dao.UserDAO;
import com.asian.billmanager.ws.json.Prefs;
import com.asian.billmanager.ws.json.ServiceResponse;

/*
 * PrefsService
 * 
 * Created: 14-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
@Path("/prefs")
public class PrefsService extends Service {
	private static final Logger logger = LogManager.getLogger(PrefsService.class.getName());
	private static final String SERVICE_NAME = "/prefs";
	private static final String MSG_FAIL_PASSWORDS_DONT_MATCH   = "Entered passwords do not match.";
	private static final String MSG_FAIL_FIRST_NAME_BLANK		= "First name cannot be left blank.";
	private static final String MSG_FAIL_LAST_NAME_BLANK		= "Last name cannot be left blank.";
	private static final String MSG_ADD_COMP_SUCCESS			= "Preferences saved successfully.";
	
	private UserDAO userDao;
	
	public PrefsService (UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	@GET
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	public Prefs getPrefs(@Context HttpServletRequest request,@QueryParam("username")String username) throws JSONException {
		logger.info("Retrieving preferences for user '"+username+"' from database");
		Prefs ret = new Prefs();
		try {
			UserBO prefsFromDB = userDao.getUserInfo(username);
			ret.setFirstName(prefsFromDB.getFirstName());
			ret.setLastName(prefsFromDB.getLastName());
			ret.setUsername(prefsFromDB.getUsername());
			ret.setPassword(prefsFromDB.getPassword());
			ret.setConfirmPassword(prefsFromDB.getPassword());
		} catch (Exception ex) {
			logger.error("Failed to retrieve user details: "+ex.getMessage());
		}
		return ret;
	}
	
	@POST
	@Path("save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceResponse savePrefs(Prefs prefRequest) throws JSONException {
		ServiceResponse response = null;
		logger.info("Saving preferences '"+prefRequest+"'");
		try {
			boolean passwordChangeRequired = false;
			boolean nameChangeRequired = false;
			final UserBO prefsFromDB = userDao.getUserInfo(prefRequest.getUsername());
			if (prefRequest.getPassword()!=null && prefRequest.getConfirmPassword()!=null) {
				if (prefRequest.getPassword().equals(prefRequest.getConfirmPassword())) {
					if(!prefRequest.getPassword().equals(prefsFromDB.getPassword())) {
						passwordChangeRequired = true;
					}
				} else {
					logger.error("Entered passwords do not match.");
					return new ServiceResponse(201,MSG_FAIL_PASSWORDS_DONT_MATCH);
				}
			} else {
				logger.error("At least one of the passwords was blank.");
				return new ServiceResponse(201,MSG_FAIL_PASSWORDS_DONT_MATCH);
			}
			if (prefRequest.getFirstName() == null || prefRequest.getFirstName().length()==0) {
				logger.error("First name is blank");
				return new ServiceResponse(202,MSG_FAIL_FIRST_NAME_BLANK);
			}
			if (prefRequest.getLastName() == null || prefRequest.getLastName().length()==0) {
				logger.error("Last name is blank");
				return new ServiceResponse(202,MSG_FAIL_LAST_NAME_BLANK);
			}
			if (!prefRequest.getFirstName().equals(prefsFromDB.getFirstName())) {
				nameChangeRequired = true;
			}
			if (!prefRequest.getLastName().equals(prefsFromDB.getLastName())) {
				nameChangeRequired = true;
			}
			int detailsChanged = 0;
			int passwordChanged = 0;
			if (nameChangeRequired) {
				detailsChanged = userDao.updateUserNameDetails(prefRequest.getUsername(),prefRequest.getFirstName(),prefRequest.getLastName());
			}
			if (passwordChangeRequired) {
				passwordChanged = userDao.updateUserPassword(prefRequest.getUsername(),DigestUtils.md5Hex(prefRequest.getPassword()));
			}
			if (detailsChanged == 1 && passwordChanged == 0) {
				return new ServiceResponse(200,"User details updated successfully.");
			}
			if (detailsChanged == 0 && passwordChanged == 1) {
				return new ServiceResponse(200,"Password changed successfully. Please logout and relogin with your new password.");
			}
			if (detailsChanged == 1 && passwordChanged == 1) {
				return new ServiceResponse(200,"User Details and Password changed successfully. Please logout and relogin with your new password.");
			}
		} catch (Exception ex) {
			logger.error("Failed to save user preferences due to Exception: "+ex.getMessage());
			return new ServiceResponse(204,"Failed to save user preferences. Error: "+ex.getMessage());
		}
		return new ServiceResponse(200,"No changes were made.");
	}
}
