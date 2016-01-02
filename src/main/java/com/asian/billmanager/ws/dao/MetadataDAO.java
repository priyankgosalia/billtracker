package com.asian.billmanager.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.asian.billmanager.ws.bo.BillFreqBO;
import com.asian.billmanager.ws.bo.ServiceHitsBO;

/*
 * MetadataDAO
 * 
 * Created: 25-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class MetadataDAO extends SuperDAO {
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	private static final String BILL_TYPE_QUERY = "select code, description from btrack.bill_freq";
	private static final String SERVICE_HITS_QUERY = "select asd.name,  count(ass.service_id) hits from audit_services_def asd, audit_services ass "+
													 "where asd.id = ass.service_id group by ass.service_id,asd.name order by 2 desc";
	
	public MetadataDAO(NamedParameterJdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public List<BillFreqBO> getBillFreqTypes() {
		return jdbcTemplate.query(BILL_TYPE_QUERY, new BillFreqExtractor());
	}
	
	public List<ServiceHitsBO> getServiceHits() {
		return jdbcTemplate.query(SERVICE_HITS_QUERY, new ServiceHitsExtractor());
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
	
	static class ServiceHitsExtractor implements ResultSetExtractor<List<ServiceHitsBO>> {
		public List<ServiceHitsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {		
			List<ServiceHitsBO> retList = new LinkedList<ServiceHitsBO>();
			if (rs!=null) {
				while(rs.next()) {
					ServiceHitsBO b = new ServiceHitsBO(getString(rs,"name"),getInteger(rs,"hits"));
					retList.add(b);
				}
			}
			return retList;
		}
	}
}
