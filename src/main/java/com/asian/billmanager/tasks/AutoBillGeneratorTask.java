package com.asian.billmanager.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asian.billmanager.app.Constants;
import com.asian.billmanager.ws.bo.BillBO;
import com.asian.billmanager.ws.dao.BillDAO;

/*
 * AutoBillGeneratorTask
 * 
 * An automated, scheduled Quartz task that runs 
 * periodically everyday to check if new bills need
 * to be auto-generated depending on their type and
 * due date. (only for recurring bills).
 * 
 * Created: 02-JAN-2016
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AutoBillGeneratorTask {
	private static final Logger logger = LogManager.getLogger(AutoBillGeneratorTask.class.getName());
	private BillDAO billDao = null;
	
	public AutoBillGeneratorTask(BillDAO billDao) {
		this.billDao = billDao;
	}
	
	private static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		return currentDate;
	}
	
	private static String getFormattedDate(Date d, String fmt) {
		String ret = null;
		try {
			DateFormat df = new SimpleDateFormat(fmt);
			ret = df.format(d);
		} catch (Exception ex) {
			logger.error("Error converting date to desired format "+ex.getMessage());
		}
		return ret;
	}
	
	public void run() {
		logger.info("*** Automated Bill Generator Task - start ***");
		final Date currentSystemDate = getCurrentDate();
		logger.info("Current Date : "+getFormattedDate(currentSystemDate,"dd-MM-yyyy"));
		List<BillBO> failedBills = new LinkedList<BillBO>();
		List<BillBO> successfulBills = new LinkedList<BillBO>();
		int masterBillCount = 0;
		try {
			// Step 1 - Get all the 'Active' + 'Recurring' bills
			logger.info("Getting list of all active recurring bills");
			List<BillBO> masterBills = billDao.getAllActiveRecurringBills();
			if (masterBills!=null && masterBills.size()>0) {
				logger.info("Retrieved "+masterBills.size()+" active recurring bills");
				// Step 2 - Iterate over each of these master bills and see if 
				// a new bill needs to be generated.
				for (BillBO mb:masterBills) {
					final int masterBillId = mb.getId();
					final String masterBillFreq = mb.getFrequency();
					logger.info("Processing master bill #"+masterBillId+", freq="+masterBillFreq);
					List<BillBO> bills = billDao.getBillListForMasterBill(masterBillId);
					if (bills!=null && bills.size()>0) {
						logger.info("Retrieved "+bills.size()+" bills for master bill # "+masterBillId);
						Collections.sort(bills, new Comparator<BillBO>() {
							  public int compare(BillBO o1, BillBO o2) {
							      return o1.getDueDate().compareTo(o2.getDueDate());
							  }
						});
						final BillBO lastBill = bills.get(bills.size()-1);
						logger.info("Latest bill: Id="+lastBill.getId()+", Due Date = "+lastBill.getDueDate());
						long diffInDays = getDifferenceDays(lastBill.getDueDate(),currentSystemDate);
						logger.info("Difference bw current date and last bill due date is "+diffInDays+" days");
						// If due date for the last bill has passed, then generate a new bill
						if (diffInDays>0) {
							// add a new bill
							Date newBillDueDate = getNewDueDateForBill(lastBill.getDueDate(),lastBill.getFrequency());
							logger.info("New Bill Due Date = "+getFormattedDate(newBillDueDate,"dd-MM-yyyy"));
							BillBO newBill = new BillBO();
							newBill.setAmount(lastBill.getAmount());
							newBill.setMasterId(masterBillId);
							newBill.setFrequency(mb.getFrequency());
							newBill.setDueDate(new java.sql.Date(newBillDueDate.getTime()));
							newBill.setPaymentMode(lastBill.getPaymentMode());
							newBill.setDescription(lastBill.getDescription());
							try {
								final int newBillId = billDao.addAutoGenBill(newBill);
								newBill.setId(newBillId);
								successfulBills.add(newBill);
							} catch(Exception ex) {
								logger.error("Failed to auto-generate bill for master bill id # "+masterBillId+": "+newBill);
								failedBills.add(newBill);
							}
						}
					} else {
						logger.info("No bills for master bill # "+masterBillId);
					}
					masterBillCount++;
				}
			} else {
				logger.info("No active recurring bills");
			}
		} catch (Exception ex) {
			logger.error("An error ocurred while running auto bill generator."+ex.getMessage());
		}
		logger.info("*** Automated Bill Generator Task - summary ***");
		logger.info("Master Bills Processed: "+masterBillCount);
		logger.info("New Bills Added (success): "+successfulBills.size());
		logger.info("New Bills Added (failure): "+failedBills.size());
		logger.info("*** Automated Bill Generator Task - end ***");
	}
	
	private static Date getNewDueDateForBill(Date dueDate, String billType) {
		Date ret = null;
		if (dueDate!=null && billType!=null) {
			if (billType.equals(Constants.BILL_TYPE_MONTHLY)) {
				ret = DateUtils.addMonths(dueDate, 1);
			} else
			if (billType.equals(Constants.BILL_TYPE_HALF_YEARLY)) {
				ret = DateUtils.addMonths(dueDate, 6);
			} else
			if (billType.equals(Constants.BILL_TYPE_QUARTERLY)) {
				ret = DateUtils.addMonths(dueDate, 3);
			} else
			if (billType.equals(Constants.BILL_TYPE_YEARLY)) {
				ret = DateUtils.addMonths(dueDate, 12);
			}
		}
		return ret;
	}
	
	private static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
}
