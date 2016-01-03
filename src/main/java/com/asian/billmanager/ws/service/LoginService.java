package com.asian.billmanager.ws.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.apache.logging.log4j.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;

import com.asian.billmanager.ws.dao.AuditDAO;
import com.asian.billmanager.ws.dao.UserDAO;
import com.asian.billmanager.ws.json.LoginRequest;
import com.asian.billmanager.ws.json.LoginResponse;
import com.asian.billmanager.ws.bo.UserBO;

/*
 * LoginService
 * 
 * Created: 12-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
@Path("/login")
public class LoginService extends Service {
	private static final Logger logger = LogManager.getLogger(LoginService.class.getName());
	
	private static final String SERVICE_NAME = "/login";
	
	private static final String MSG_LOGIN_FAILED 				= "Login failed. Invalid username/password.";
	private static final String MSG_INSUFFICIENT_CREDENTIALS 	= "Insufficient credentials provided.";
	private static final String MSG_ACCOUNT_DISABLED		 	= "Sorry! Your account has been disabled by the administrator.";
	private static final String MSG_DATABASE_ISSUE 				= "Login failed. Unable to connect to the Database.";

	private UserDAO userDAO;
	private AuditDAO auditDAO;
	
	public LoginService(UserDAO userDao, AuditDAO auditDao) {
		this.userDAO = userDao;
		this.auditDAO = auditDao;
	}
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	@POST
	@Path("auth")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse authenticate(LoginRequest request,
									@Context HttpServletRequest req) throws JSONException {
		final String username = request.getUsername();
		final String password = request.getPassword();
		LoginResponse response = null;
		if (username!=null && password!=null) {
			final String passwordHash = DigestUtils.md5Hex(password);
			logger.info("Logging in User = '"+username+"' with Password(hashed) = '"+passwordHash+"'");
			try {
				final UserBO u = userDAO.getUserInfo(username);
				if (u!=null) {
					// Check if user is allowed to Login
					if (u.isEnabled()) {
						if (passwordHash!=null && u.getPassword()!=null) {
							if (u.getPassword().equals(passwordHash)) {
								logger.info("Login successful for user '"+username+"'.");
								response = LoginResponse.getSuccessResponseWithMessage("Login successful for user '"+u.getUsername()+"'.");
								response.setUserFirstName(u.getFirstName());
								response.setUserId(u.getId());
								response.setAdmin(u.isAdmin());
								req.getSession().setAttribute(ServiceConstants.SESSION_OBJ_CURRENT_USER_ID, u.getId());
								req.getSession().setAttribute(ServiceConstants.SESSION_OBJ_CURRENT_USER_NAME, u.getUsername());
								auditDAO.addLoginLog(u.getId(), 0);
							} else {
								logger.error("Login failed for user '"+username+"' due to invalid password.");
								auditDAO.addLoginLog(u.getId(), -1);
								response = LoginResponse.getFailureResponseWithMessage(MSG_LOGIN_FAILED);
							}
						} else {
							logger.error("Either username or password is not correctly set in the database for user '"+username+"'.");
							response = LoginResponse.getFailureResponseWithMessage(MSG_LOGIN_FAILED);
						}
					} else {
						logger.warn("The account '"+u.getUsername()+"' has been disabled.");
						response = LoginResponse.getFailureResponseWithMessage(MSG_ACCOUNT_DISABLED);
					}
				} else {
					logger.error("Login failed for user '"+username+"' as user doesn't exist in the database.");
					response = LoginResponse.getFailureResponseWithMessage(MSG_LOGIN_FAILED);
				}
			} catch (Exception ex) {
				logger.error("Failed to query the database. "+ex.getMessage());
				response = LoginResponse.getFailureResponseWithMessage(MSG_DATABASE_ISSUE);
			}
		} else {
			logger.error("Username/Password is null in JSON Request");
			response = LoginResponse.getFailureResponseWithMessage(MSG_INSUFFICIENT_CREDENTIALS);
		}
		return response;
	}
}
