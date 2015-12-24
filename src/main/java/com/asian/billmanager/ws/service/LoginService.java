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
			logger.info("User = '"+username+"', Password = '"+password+"'");
			final UserBO u = userDAO.getUserInfo(username);
			if (u!=null) {
				final String usermd5 = DigestUtils.md5Hex(password);
				if (usermd5!=null && u.getPassword()!=null) {
					if (u.getPassword().equals(usermd5)) {
						logger.info("Login successful for user '"+username+"'.");
						response = LoginResponse.getSuccessResponseWithMessage("Login successful.");
						response.setUserFirstName(u.getFirstName());
						response.setUserId(u.getId());
						req.getSession().setAttribute(ServiceConstants.SESSION_OBJ_CURRENT_USER_ID, u.getId());
						req.getSession().setAttribute(ServiceConstants.SESSION_OBJ_CURRENT_USER_NAME, u.getUsername());
						auditDAO.addLoginLog(u.getId(), 0);
					} else {
						logger.error("Login failed for user '"+username+"' due to invalid password.");
						response = LoginResponse.getFailureResponseWithMessage(MSG_LOGIN_FAILED);
						auditDAO.addLoginLog(u.getId(), -1);
					}
				} else {
					response = LoginResponse.getFailureResponseWithMessage(MSG_LOGIN_FAILED);
				}
			} else {
				logger.error("Login failed for user '"+username+"' as user doesn't exist in the database.");
				response = LoginResponse.getFailureResponseWithMessage(MSG_LOGIN_FAILED);
			}
		} else {
			response = LoginResponse.getFailureResponseWithMessage(MSG_INSUFFICIENT_CREDENTIALS);
		}
		return response;
	}
}
