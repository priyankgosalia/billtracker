package com.asian.billmanager.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.asian.billmanager.ws.bo.UserBO;
/*
 * UserDAO
 * 
 * Created: 21-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class UserDAO extends SuperDAO {
	private static final String USER_BY_USERNAME_QUERY = "SELECT id,username,password,firstname,lastname,enabled,is_admin from btrack.users where username=:username";
	private static final String UPDATE_USER_DETAIL_QUERY = "UPDATE btrack.users set firstname=:firstname,lastname=:lastname where username=:username";
	private static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE btrack.users set password=:password where username=:username";
	
	private NamedParameterJdbcTemplate jdbcTemplate = null;
	
	public UserDAO(NamedParameterJdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	public UserBO getUserInfo(String username) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("username",username);
		return jdbcTemplate.query(USER_BY_USERNAME_QUERY, paramMap, new UserExtractor());
	}
	
	public int updateUserNameDetails(String username, String firstName, String lastName) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("username",username);
		paramMap.put("firstname",firstName);
		paramMap.put("lastname",lastName);
		return jdbcTemplate.update(UPDATE_USER_DETAIL_QUERY, paramMap);
	}
	
	public int updateUserPassword(String username, String md5Password) throws Exception {
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("username",username);
		paramMap.put("password",md5Password);
		return jdbcTemplate.update(UPDATE_USER_PASSWORD_QUERY, paramMap);
	}
	
	static class UserExtractor implements ResultSetExtractor<UserBO> {
		public UserBO extractData(ResultSet rs) throws SQLException, DataAccessException {			
			if (rs!=null) {
				if(rs.next()) {
					UserBO u = new UserBO();
					u.setId(getInteger(rs,"id"));
					u.setUsername(getString(rs,"username"));
					u.setPassword(getString(rs,"password"));
					u.setFirstName(getString(rs,"firstname"));
					u.setLastName(getString(rs,"lastname"));
					u.setEnabled(getInteger(rs,"enabled")==1?true:false);
					u.setAdmin(getInteger(rs,"is_admin")==1?true:false);
					return u;
				}
			}
			return null;
		}
	}
}
