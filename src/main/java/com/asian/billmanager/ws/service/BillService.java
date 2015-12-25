package com.asian.billmanager.ws.service;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import com.asian.billmanager.ws.bo.BillBO;
import com.asian.billmanager.ws.dao.BillDAO;
import com.asian.billmanager.ws.json.Bill;

/*
 * BillService
 * 
 * Created: 25-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
@Path("/bill")
public class BillService extends Service {
	private static final String SERVICE_NAME = "/bill";
	private static final Logger logger = LogManager.getLogger(BillService.class.getName());
	
	private BillDAO billDAO = null;
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	public BillService (BillDAO billDAO) {
		this.billDAO = billDAO;
	}

	@GET
	@Path("getAllBills")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bill> getAllBills(@Context HttpServletRequest request) throws JSONException {
		logger.info("Retrieving all bills from database");
		List<Bill> list = new LinkedList<Bill>();
		List<BillBO> billListFromDB = billDAO.getAllBills();
		if (billListFromDB!=null && billListFromDB.size()>0) {
			for (BillBO b:billListFromDB) {
				Bill bx = new Bill();
				bx.setId(b.getId());
				bx.setMasterId(b.getMasterId());
				bx.setAmount(b.getAmount());
				bx.setDescription(b.getDescription());
				bx.setLocation(b.getLocation());
				bx.setCompany(b.getCompany());
				bx.setFrequency(b.getFrequency());
				bx.setPaymentMode(b.getPaymentMode());
				bx.setStatus(b.getStatus());
				bx.setUser(b.getUser());
				bx.setDueDate(b.getDueDate());
				bx.setDueDay(b.getDueDay());
				list.add(bx);
			}
		}
		logger.info("Retrieved "+list.size()+" bills from database.");
		return list;
	}
}
