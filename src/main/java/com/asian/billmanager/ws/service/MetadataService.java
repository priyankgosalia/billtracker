package com.asian.billmanager.ws.service;

import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.asian.billmanager.version.Version;
import com.asian.billmanager.ws.bo.BillFreqBO;
import com.asian.billmanager.ws.dao.MetadataDAO;
import com.asian.billmanager.ws.json.AboutResponse;
import com.asian.billmanager.ws.json.BillTypeResponse;

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
	private MetadataDAO metadataDAO;
	
	public MetadataService(MetadataDAO metadataDao) {
		this.metadataDAO = metadataDao;
	}
	
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
	
	@GET
	@Path("billType")
	@Produces(MediaType.APPLICATION_JSON)
	public List<BillTypeResponse> billType() throws JSONException {
		List<BillTypeResponse> retList = new LinkedList<BillTypeResponse>();
		try {
			List<BillFreqBO> billTypeList = metadataDAO.getBillFreqTypes();
			for (BillFreqBO bf:billTypeList) {
				retList.add(new BillTypeResponse(bf.getType(),bf.getDesc()));
			}
		} catch (Exception ex) {
			logger.error("Failed to retrieve bill type information from database. "+ex.getMessage());
		}
		
		return retList;
	}
	
	@GET
	@Path("audit/ServiceHits")
	@Produces(MediaType.APPLICATION_JSON)
	public List<BillTypeResponse> getServiceHits() throws JSONException {
		List<BillTypeResponse> retList = new LinkedList<BillTypeResponse>();
		try {
			List<BillFreqBO> billTypeList = metadataDAO.getBillFreqTypes();
			for (BillFreqBO bf:billTypeList) {
				retList.add(new BillTypeResponse(bf.getType(),bf.getDesc()));
			}
		} catch (Exception ex) {
			logger.error("Failed to retrieve bill type information from database. "+ex.getMessage());
		}
		
		return retList;
	}
}
