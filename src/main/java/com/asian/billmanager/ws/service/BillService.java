package com.asian.billmanager.ws.service;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import com.asian.billmanager.ws.bo.BillBO;
import com.asian.billmanager.ws.bo.ReminderBO;
import com.asian.billmanager.ws.dao.BillDAO;
import com.asian.billmanager.ws.json.AddBillRequest;
import com.asian.billmanager.ws.json.AddBillResponse;
import com.asian.billmanager.ws.json.Bill;
import com.asian.billmanager.ws.json.DeleteBillRequest;
import com.asian.billmanager.ws.json.DeleteBillResponse;
import com.asian.billmanager.ws.json.Reminder;

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
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String TIMESTAMP_FORMAT = "dd/MMM/yyyy hh:mm a";
	private static final DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	private static final DateFormat tf = new SimpleDateFormat(TIMESTAMP_FORMAT);
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
		final List<Bill> list = new LinkedList<Bill>();
		List<BillBO> billListFromDB = billDAO.getAllBills();
		if (billListFromDB!=null && billListFromDB.size()>0) {
			for (BillBO b:billListFromDB) {
				Bill bx = populateBillInfoObject(b);
				list.add(bx);
			}
		}
		logger.info("Retrieved "+list.size()+" bills from the database.");
		return list;
	}
	
	@GET
	@Path("getAllDeletedBills")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bill> getAllDeletedBills(@Context HttpServletRequest request) throws JSONException {
		logger.info("Retrieving all bills from database");
		final List<Bill> list = new LinkedList<Bill>();
		List<BillBO> billListFromDB = billDAO.getAllDeletedBills();
		if (billListFromDB!=null && billListFromDB.size()>0) {
			for (BillBO b:billListFromDB) {
				Bill bx = populateBillInfoObject(b);
				list.add(bx);
			}
		}
		logger.info("Retrieved "+list.size()+" bills from the database.");
		return list;
	}
	
	@GET
	@Path("getAllReminders")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Reminder> getAllReminders(@Context HttpServletRequest request) throws JSONException {
		logger.info("Retrieving all reminders from database");
		final List<Reminder> list = new LinkedList<Reminder>();
		List<ReminderBO> remindersList = billDAO.getAllReminders();
		if (remindersList!=null && remindersList.size()>0) {
			for (ReminderBO rm:remindersList) {
				Bill bx = populateBillInfoObject(rm.getBill());
				Reminder rx = new Reminder();
				rx.setBill(bx);
				rx.setId(rm.getReminderId());
				rx.setMasterReminderId(rm.getMasterReminderId());
				rx.setDaysRemaining(rm.getDueDays());
				list.add(rx);
			}
		}
		logger.info("Retrieved "+list.size()+" reminders from the database.");
		return list;
	}
	
	@GET
	@Path("billInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public Bill getBillInfo(@QueryParam("id") int billId,
			@Context HttpServletRequest request) throws JSONException {
		logger.info("Retrieving Bill info for Bill # "+billId+" from the database");
		Bill bx = null;
		try {
			BillBO b = billDAO.getBillInfo(billId);
			if (b!=null) {
				bx = populateBillInfoObject(b);
				logger.info("Retrieved info for Bill # "+billId+" from database.");
			} else {
				logger.error("Failed to retrieve info for Bill # "+billId+" from database.");
			}
		} catch (Exception ex) {
			logger.error("Error ocurred while retrieving bill info for Bill # "+billId);
		}
		return bx;
	}
	
	@GET
	@Path("billInfoForEdit")
	@Produces(MediaType.APPLICATION_JSON)
	public Bill getBillInfoForEdit(@QueryParam("id") int billId,
			@Context HttpServletRequest request) throws JSONException {
		logger.info("Retrieving Bill info for Edit Bill # "+billId+" from the database");
		Bill bx = null;
		try {
			BillBO b = billDAO.getBillInfoForEdit(billId);
			if (b!=null) {
				bx = populateBillInfoObject(b);
				logger.info("Retrieved info for Edit Bill # "+billId+" from database.");
			} else {
				logger.error("Failed to retrieve info for Edit Bill # "+billId+" from database.");
			}
		} catch (Exception ex) {
			logger.error("Error ocurred while retrieving edit bill info for Bill # "+billId);
		}
		return bx;
	}
	
	@POST
	@Path("addBill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AddBillResponse addBill(AddBillRequest request,
									@Context HttpServletRequest req) throws JSONException {
		logger.info("Adding new bill "+request);
		try {
			final Date dueDate = df.parse(request.getDueDate());
			final int userId = Integer.parseInt(request.getUserId());
			final int billId = billDAO.addBill(request.getCompanyId(),
							request.getLocation(),
							request.getBillType(),
							request.getAmount(),
							request.getPaymentMode(),
							userId,
							request.getDescription(),
							dueDate.getDate(),
							new java.sql.Date(dueDate.getTime()),
							(request.isRecurrence())?1:0,
							request.getPaid(),
							request.getReminderDays());
			return AddBillResponse.getSuccessResponseWithMessageAndBillId("Bill added successfully.",billId);
		} catch(ParseException ex) {
			logger.error("Failed to add bill due to an error while parsing due date. "+ex.getMessage());
			return AddBillResponse.getFailureResponseWithMessage("Failed to add Bill. The format of Due Date is incorrect.");
		} catch(Exception ex) {
			return AddBillResponse.getFailureResponseWithMessage("Failed to add Bill. Error: "+ex.getMessage());
		}
	}
	
	@POST
	@Path("updateBill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AddBillResponse updateBill(AddBillRequest request,
									@Context HttpServletRequest req) throws JSONException {
		logger.info("Updating details of bill "+request);
		try {
			final Date dueDate = df.parse(request.getDueDate());
			final int userId = Integer.parseInt(request.getUserId());
			final int billId = billDAO.updateBill(request.getBillId(),
							request.getCompanyId(),
							request.getLocation(),
							request.getBillType(),
							request.getAmount(),
							request.getPaymentMode(),
							userId,
							request.getDescription(),
							dueDate.getDate(),
							new java.sql.Date(dueDate.getTime()),
							(request.isRecurrence())?1:0,
							request.getPaid(),
							request.getReminderDays());
			return AddBillResponse.getSuccessResponseWithMessageAndBillId("Bill added successfully.",billId);
		} catch(ParseException ex) {
			logger.error("Failed to save changes due to an error while parsing due date. "+ex.getMessage());
			return AddBillResponse.getFailureResponseWithMessage("Failed to save changes to Bill. The format of Due Date is incorrect.");
		} catch(Exception ex) {
			return AddBillResponse.getFailureResponseWithMessage("Failed to save changes to Bill. Error: "+ex.getMessage());
		}
	}
	
	@GET
	@Path("markPaid")
	@Produces(MediaType.APPLICATION_JSON)
	public AddBillResponse markPaid(@QueryParam("id") int billId,
									@Context HttpServletRequest req) throws JSONException {
		logger.info("Marking bill "+billId+" as paid");
		try {
			billDAO.markPaid(billId);
			return AddBillResponse.getSuccessResponseWithMessageAndBillId("Bill marked as Paid successfully.",billId);
		} catch(Exception ex) {
			return AddBillResponse.getFailureResponseWithMessage("Failed to mark Bill as Paid. Error: "+ex.getMessage());
		}
	}
	
	@POST
	@Path("deleteBill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DeleteBillResponse deleteBill(DeleteBillRequest request,
									@Context HttpServletRequest req) throws JSONException {
		logger.info("Deleting bill "+request);
		try {
			int rowsAffected = billDAO.deleteBill(request.getBillId(),request.isDeleteRecur());
			if (rowsAffected>0){
				return DeleteBillResponse.getSuccessResponseWithMessage("Bill deleted successfully.");
			} else {
				return DeleteBillResponse.getFailureResponseWithMessage("Failed to delete Bill.");
			}
		} catch(Exception ex) {
			return DeleteBillResponse.getFailureResponseWithMessage("Failed to delete Bill. Error: "+ex.getMessage());
		}
	}
	
	private Bill populateBillInfoObject(BillBO b) {
		final Bill bx = new Bill();
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
		bx.setDeleted(b.isDeleted());
		bx.setCreationDate(tf.format(b.getCreationDate()));
		bx.setRecurring(b.isRecurring());
		bx.setAutoGenerated(b.isAutoGenerated());
		bx.setReminderDays(b.getReminderDays());
		return bx;
	}
}
