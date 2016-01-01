package com.asian.billmanager.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.asian.billmanager.ws.bo.BillFreqBO;

/*
 * MetadataDAO
 * 
 * Created: 25-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class MetadataDAO extends SuperDAO {
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	private static final String BILL_TYPE_QUERY = "select code, description from btrack.bill_freq";
	
	public MetadataDAO(NamedParameterJdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public List<BillFreqBO> getBillFreqTypes() {
		return jdbcTemplate.query(BILL_TYPE_QUERY, new BillFreqExtractor());
	}
	
	static class BillFreqExtractor implements ResultSetExtractor<List<BillFreqBO>> {
		public List<BillFreqBO> extractData(ResultSet rs) throws SQLException, DataAccessException {		
			List<BillFreqBO> retList = new LinkedList<BillFreqBO>();
			if (rs!=null) {
				while(rs.next()) {
					BillFreqBO b = new BillFreqBO();
					b.setType(getString(rs,"code"));
					b.setDesc(getString(rs,"description"));
					retList.add(b);
				}
			}
			return retList;
		}
	}
}
