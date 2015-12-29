package com.asian.billmanager.ws.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asian.billmanager.ws.bo.ServiceBO;
import com.asian.billmanager.ws.context.ApplicationContextProvider;
import com.asian.billmanager.ws.dao.AuditDAO;
import com.asian.billmanager.ws.service.ServiceConstants;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/*
 * AuditFilter
 * 
 * Created: 24-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AuditFilter implements ContainerRequestFilter {
	private static final Logger logger = LogManager.getLogger(AuditFilter.class.getName());
	
	@Context
	HttpServletRequest servletRequest;
	
	public ContainerRequest filter(ContainerRequest request) {
		if (!request.getRequestUri().toString().contains("/login") && !request.getRequestUri().toString().contains("/metadata")) {
			final int userId = (Integer)servletRequest.getSession().getAttribute(ServiceConstants.SESSION_OBJ_CURRENT_USER_ID);
			final String requestURI = servletRequest.getRequestURI().substring(servletRequest.getRequestURI().indexOf("/ws"));
			logServiceInvocationToDB(requestURI, userId);
		}
		return request;
	}
	
	private void logServiceInvocationToDB(String serviceName, int userId) {
		AuditDAO auditDao = ApplicationContextProvider.getCtx().getBean(AuditDAO.class);
		if(auditDao!=null) {
			Map<String,ServiceBO> serviceList = auditDao.getAllServices();
			boolean serviceDefPresent = true;
			if (!serviceList.keySet().contains(serviceName)) {
				logger.info("Service definition for "+serviceName+"' is not present in the database.");
				if (!auditDao.addServiceDef(serviceName)) {
					serviceDefPresent = false;
					logger.error("Failed to add service definition for "+serviceName+" to the database.");
				} else {
					serviceList = auditDao.getAllServices();
				}
			}
			if (serviceDefPresent) {
				ServiceBO svc = serviceList.get(serviceName);
				if (svc!=null) {
					logger.debug("Recording audit log "+svc.getName()+" for userId="+userId);
					auditDao.addServiceLog(userId, svc.getId());
				}
			}
		} else {
			logger.error("Unable to log service invocation audit entry to Database. DAO is undefined.");
		}
	}
}
