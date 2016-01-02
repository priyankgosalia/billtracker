package com.asian.billmanager.ws.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import com.asian.billmanager.ws.bo.BillBO;

/*
 * BillDAO
 * 
 * Created: 25-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class BillDAO extends SuperDAO {
	private static final Logger logger = LogManager.getLogger(BillDAO.class.getName());
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	private SimpleJdbcTemplate sjdbcTemplate = null;
	private static final String ALL_BILLS_QUERY = "select b.id,bm.id master_bill_id,c.name company,b.amount,u.firstName addedby,bf.description frequency, "+
												"bm.payment_mode, bm.location, bm.description bill_desc, b.paid, b.deleted, bm.due_day, b.due_date, b.creation_date, bm.auto_recur "+
												"from btrack.bill_master bm, bill b, bill_freq bf, users u, company c "+
												"where b.master_bill_id = bm.id and bm.freq_id = bf.id and bm.user_id = u.id "+
												"and bm.company_id = c.id order by b.id desc";
	private static final String NEW_BILL_MASTER_QUERY = "insert into bill_master(company_id,location,freq_id,amount,"+
														"payment_mode,user_id,description,due_day,due_date,auto_recur) values ("+
														":company_id,:location,(select id from btrack.bill_freq where code=:freq_type),:amount,:payment_mode,:user_id,"+
														":desc,:due_day,:due_date,:auto_recur)";
	private static final String NEW_BILL_INSTANCE_QUERY = "insert into bill(master_bill_id,amount,due_date,paid,deleted) values ("+
															":master_bill_id,:amount,:due_date,:paid,0)";
	private static final String LAST_ID_QUERY = "select last_insert_id()";
	private static final String VIEW_BILL_QUERY = "select b.id,bm.id master_bill_id,c.name company,b.amount,u.firstName addedby, bf.description frequency, "+
												"bm.payment_mode, bm.location, bm.description bill_desc, b.paid, b.deleted, bm.due_day, b.due_date, b.creation_date, bm.auto_recur "+
												"from btrack.bill_master bm, bill b, bill_freq bf, users u, company c where b.master_bill_id = bm.id and "+
												"bm.freq_id = bf.id and bm.user_id = u.id and bm.company_id = c.id and b.id=:bill_id";
	
	public BillDAO(NamedParameterJdbcTemplate template, SimpleJdbcTemplate stemplate) {
		this.jdbcTemplate = template;
		this.sjdbcTemplate = stemplate;
	}
	
	public List<BillBO> getAllBills() {
		return jdbcTemplate.query(ALL_BILLS_QUERY, new AllBillsExtractor());
	}
	
	public BillBO getBillInfo(int billId) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		logger.info("Querying the database for Bill #"+billId);
		paramMap.put("bill_id",billId);
		try {
			return jdbcTemplate.query(VIEW_BILL_QUERY, paramMap, new BillInfoExtractor());
		} catch (DataAccessException dex) {
			logger.error("Error querying the Bill table(s): "+dex.getMessage());
			throw new Exception (dex.getMessage());
		}
	}
	
	@Transactional(rollbackFor={Exception.class,DataAccessException.class})
	public int addBill(int companyId, String location, char freqType, Double amount,
						String paymentMode, int userId, String desc, int dueDay,
						Date dueDate, int autoRecur, int paid) throws Exception {
		try {
			// Step 1 - Insert into BILL_MASTER
			logger.info("Inserting an entry into BILL_MASTER - Start");
			Map<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("company_id",companyId);
			paramMap.put("location",location);
			paramMap.put("freq_type",""+freqType);
			paramMap.put("amount",amount);
			paramMap.put("payment_mode",paymentMode);
			paramMap.put("user_id",userId);
			paramMap.put("desc",desc);
			paramMap.put("due_day",dueDay);
			paramMap.put("due_date",dueDate);
			paramMap.put("auto_recur",autoRecur);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(NEW_BILL_MASTER_QUERY, paramMap);
			logger.info("Inserting an entry into BILL_MASTER - End");
			// Step 2 - Insert into BILL
			logger.info("Inserting an entry into BILL - Start");
			int lastBillMasterId = sjdbcTemplate.queryForObject(LAST_ID_QUERY,Integer.class);
			logger.info("Last generated bill master id = "+lastBillMasterId);
			paramMap.put("master_bill_id", lastBillMasterId);
			paramMap.put("paid", paid);
			jdbcTemplate.update(NEW_BILL_INSTANCE_QUERY, paramMap);
			logger.info("Inserting an entry into BILL - End");
			int lastBillInstanceId = sjdbcTemplate.queryForObject(LAST_ID_QUERY,Integer.class);
			logger.info("Last generated bill instance id = "+lastBillInstanceId);
			return lastBillInstanceId;
		} catch (DataAccessException dex) {
			logger.error("Error inserting entry into one of the bill tables: "+dex.getMessage());
			throw new Exception (dex.getMessage());
		}
	}
	
	static class AllBillsExtractor implements ResultSetExtractor<List<BillBO>> {
		public List<BillBO> extractData(ResultSet rs) throws SQLException, DataAccessException {		
			List<BillBO> retList = new LinkedList<BillBO>();
			if (rs!=null) {
				while(rs.next()) {
					BillBO b = populateBillBOFromResultSet(rs);
					retList.add(b);
				}
			}
			return retList;
		}
	}
	
	static class BillInfoExtractor implements ResultSetExtractor<BillBO> {
		public BillBO extractData(ResultSet rs) throws SQLException, DataAccessException {		
			if (rs!=null) {
				if(rs.next()) {
					BillBO b = populateBillBOFromResultSet(rs);
					return b;
				}
			}
			return null;
		}
	}
	
	private static BillBO populateBillBOFromResultSet(ResultSet rs) throws SQLException {
		BillBO b = new BillBO();
		b.setId(getInteger(rs,"id"));
		b.setMasterId(getInteger(rs,"master_bill_id"));
		b.setCompany(getString(rs,"company"));
		b.setAmount(getDouble(rs,"amount"));
		b.setLocation(getString(rs,"location"));
		b.setUser(getString(rs,"addedby"));
		b.setFrequency(getString(rs,"frequency"));
		b.setDescription(getString(rs,"bill_desc"));
		b.setPaymentMode(getString(rs,"payment_mode"));
		b.setDueDay(getInteger(rs,"due_day"));
		b.setDueDate(getDate(rs,"due_date"));
		final int billStatus = getInteger(rs,"paid");
		if (billStatus!=1) {
			b.setStatus("Unpaid");
		} else {
			b.setStatus("Paid");
		}
		final int billDeleted = getInteger(rs,"deleted");
		if (billDeleted!=1) {
			b.setDeleted(false);
		} else {
			b.setDeleted(true);
		}
		final int recurring = getInteger(rs,"auto_recur");
		if (recurring!=1) {
			b.setRecurring(false);
		} else {
			b.setRecurring(true);
		}
		b.setCreationDate(getTimestamp(rs,"creation_date"));
		return b;
	}
}
