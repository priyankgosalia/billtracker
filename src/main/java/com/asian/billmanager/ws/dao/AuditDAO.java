package com.asian.billmanager.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.asian.billmanager.ws.bo.ServiceBO;

/*
 * AuditDAO
 * 
 * Created: 24-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public final class AuditDAO extends SuperDAO {
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	private static final String ALL_SERVICES_QUERY 	= "select id, name from btrack.audit_services_def";
	private static final String SERVICE_DEF_QUERY 	= "insert into btrack.audit_services_def (name) values (:name)";
	private static final String AUDIT_SERVICES_INSERT_QUERY = "insert into btrack.audit_services (service_id,user_id) values (:service_id,:user_id)";
	
	public AuditDAO(NamedParameterJdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public boolean addServiceDef(String serviceName) {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("name",serviceName);
		if (jdbcTemplate.update(SERVICE_DEF_QUERY,paramMap)>0) {
			return true;
		}
		return false;
	}
	
	public boolean addServiceLog(Integer userId, Integer serviceId) {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("user_id",userId);
		paramMap.put("service_id",serviceId);
		if (jdbcTemplate.update(AUDIT_SERVICES_INSERT_QUERY,paramMap)>0) {
			return true;
		}
		return false;
	}
	
	public Map<String,ServiceBO> getAllServices() {
		return jdbcTemplate.query(ALL_SERVICES_QUERY, new AllServicesExtractor());
	}
	
	static class AllServicesExtractor implements ResultSetExtractor<Map<String,ServiceBO>> {
		public Map<String,ServiceBO> extractData(ResultSet rs) throws SQLException, DataAccessException {		
			Map<String,ServiceBO> retList = new HashMap<String,ServiceBO>();
			if (rs!=null) {
				while(rs.next()) {
					ServiceBO c = new ServiceBO();
					c.setId(getInteger(rs,"id"));
					c.setName(getString(rs,"name"));
					retList.put(c.getName(),c);
				}
			}
			return retList;
		}
	}
}
