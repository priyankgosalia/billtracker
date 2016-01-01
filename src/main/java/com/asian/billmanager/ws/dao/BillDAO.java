package com.asian.billmanager.ws.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.asian.billmanager.ws.bo.BillBO;

/*
 * BillDAO
 * 
 * Created: 25-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class BillDAO extends SuperDAO {
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	private static final String ALL_BILLS_QUERY = "select b.id,bm.id master_id,c.name company,b.amount,u.firstName addedby,bf.description frequency, "+
												"bm.payment_mode, bm.location, bm.description bill_desc, b.status, bm.due_day, bm.due_date "+
												"from btrack.bill_master bm, bill b, bill_freq bf, users u, company c "+
												"where b.master_id = bm.id and bm.freq_id = bf.id and bm.user_id = u.id "+
												"and bm.company_id = c.id order by b.id desc";
	private static final String NEW_BILL_MASTER_QUERY = "insert into bill_master(company_id,location,freq_id,amount,"+
														"payment_mode,user_id,description,due_day,due_date,auto_recur) values ("+
														":company_id,:location,(select id from btrack.bill_freq where code=:freq_type),:amount,:payment_mode,:user_id,"+
														":desc,:due_day,:due_date,:auto_recur)";
	
	public BillDAO(NamedParameterJdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public List<BillBO> getAllBills() {
		return jdbcTemplate.query(ALL_BILLS_QUERY, new AllBillsExtractor());
	}
	
	public int addBill(int companyId, String location, char freqType, Double amount,
						String paymentMode, int userId, String desc, int dueDay,
						Date dueDate, int autoRecur) {
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
		return jdbcTemplate.update(NEW_BILL_MASTER_QUERY, paramMap);
	}
	
	static class AllBillsExtractor implements ResultSetExtractor<List<BillBO>> {
		public List<BillBO> extractData(ResultSet rs) throws SQLException, DataAccessException {		
			List<BillBO> retList = new LinkedList<BillBO>();
			if (rs!=null) {
				while(rs.next()) {
					BillBO b = new BillBO();
					b.setId(getInteger(rs,"id"));
					b.setMasterId(getInteger(rs,"master_id"));
					b.setCompany(getString(rs,"company"));
					b.setAmount(getDouble(rs,"amount"));
					b.setLocation(getString(rs,"location"));
					b.setUser(getString(rs,"addedby"));
					b.setFrequency(getString(rs,"frequency"));
					b.setDescription(getString(rs,"bill_desc"));
					b.setPaymentMode(getString(rs,"payment_mode"));
					b.setDueDay(getInteger(rs,"due_day"));
					b.setDueDate(getDate(rs,"due_date"));
					final String billStatus = getString(rs,"status");
					if ("N".equals(billStatus)) {
						b.setStatus("Unpaid");
					} else if ("Y".equals(billStatus)) {
						b.setStatus("Paid");
					}
					
					retList.add(b);
				}
			}
			return retList;
		}
	}
}
