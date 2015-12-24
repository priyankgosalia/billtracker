package com.asian.billmanager.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.asian.billmanager.ws.bo.CompanyBO;

/*
 * CompanyDAO
 * 
 * Created: 21-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class CompanyDAO extends SuperDAO {
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	
	private static final String ALL_COMPANY_QUERY 		= "select id, name from btrack.company order by name";
	private static final String COMPANY_BY_NAME_QUERY 	= "select id, name from btrack.company where lower(name) = lower(:name)";
	private static final String ADD_COMPANY_QUERY		= "insert into btrack.company (name) values (:name)";
	
	public CompanyDAO(NamedParameterJdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public List<CompanyBO> getCompanyList() {
		return jdbcTemplate.query(ALL_COMPANY_QUERY, new CompanyListExtractor());
	}
	
	public CompanyBO getCompanyByName(String companyName) {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("name",companyName);
		return jdbcTemplate.query(COMPANY_BY_NAME_QUERY, paramMap, new CompanyExtractor());
	}
	
	public int addCompany(String companyName) {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("name",companyName);
		return jdbcTemplate.update(ADD_COMPANY_QUERY, paramMap);
	}
	
	static class CompanyExtractor implements ResultSetExtractor<CompanyBO> {
		public CompanyBO extractData(ResultSet rs) throws SQLException, DataAccessException {
			CompanyBO c = null;
			if (rs!=null) {
				if(rs.next()) {
					c = new CompanyBO();
					c.setId(getInteger(rs,"id"));
					c.setName(getString(rs,"name"));
				}
			}
			return c;
		}
	}
	
	static class CompanyListExtractor implements ResultSetExtractor<List<CompanyBO>> {
		public List<CompanyBO> extractData(ResultSet rs) throws SQLException, DataAccessException {		
			List<CompanyBO> retList = new LinkedList<CompanyBO>();
			if (rs!=null) {
				while(rs.next()) {
					CompanyBO c = new CompanyBO();
					c.setId(getInteger(rs,"id"));
					c.setName(getString(rs,"name"));
					retList.add(c);
				}
			}
			return retList;
		}
	}
}
