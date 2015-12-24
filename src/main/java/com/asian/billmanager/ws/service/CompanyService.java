package com.asian.billmanager.ws.service;

import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.asian.billmanager.ws.bo.CompanyBO;
import com.asian.billmanager.ws.dao.CompanyDAO;
import com.asian.billmanager.ws.json.Company;
import com.asian.billmanager.ws.json.ServiceResponse;

/*
 * CompanyService
 * 
 * Created: 19-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
@Path("/company")
public class CompanyService extends Service {
	private static final Logger logger = LogManager.getLogger(CompanyService.class.getName());
	private static final String SERVICE_NAME = "/company";
	private static final String MSG_ADD_COMP_FAIL 				= "Failed to add the Company due to a system error.";
	private static final String MSG_ADD_COMP_SUCCESS			= "added successfully.";
	
	private CompanyDAO companyDAO;
	
	public CompanyService (CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	@GET
	@Path("getCompanyList")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Company> getCompanyList() throws JSONException {
		List<Company> list = new LinkedList<Company>();
		List<CompanyBO> compListFromDB = companyDAO.getCompanyList();
		if (compListFromDB!=null && compListFromDB.size()>0) {
			for (CompanyBO c:compListFromDB) {
				list.add(new Company(c.getId(),c.getName()));
			}
		}
		
		return list;
	}
	
	@PUT
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)
	public ServiceResponse addCompany(@QueryParam("name") String name) throws JSONException {
		logger.info("Adding company '"+name+"'");
		CompanyBO company = companyDAO.getCompanyByName(name);
		if (company!=null) {
			logger.error("Failed to add company '"+name+"' (already exists).");
			return new ServiceResponse(-1,"The Company '"+name+"' is already present in the system.");
		} else {
			if (companyDAO.addCompany(name) == 1) {
				logger.info("Added new company '"+name+"'. successfully.");
				return new ServiceResponse(200,name+" "+MSG_ADD_COMP_SUCCESS);
			} else {
				logger.info("Failed to add company '"+name+"'. due to database error.");
				return new ServiceResponse(201,MSG_ADD_COMP_FAIL);
			}
		}
	}
}
