package com.asian.billmanager.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * SuperDAO - Daddy of all DAOs
 * 
 * Created: 22-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class SuperDAO {
	protected static Integer getInteger(ResultSet rs, String columnName) throws SQLException {
		int val = rs.getInt(columnName);
		return rs.wasNull() ? Integer.valueOf(0) : Integer.valueOf(val);
	}
	
	protected static Long getLong(ResultSet rs, String columnName) throws SQLException {
		Long val = rs.getLong(columnName);
		return rs.wasNull() ? Long.valueOf(0) : Long.valueOf(val);
	}
	
	protected static String getString(ResultSet rs, String columnName) throws SQLException {
		final String ret = rs.getString(columnName);
		if (ret!=null && !ret.isEmpty()) {
			return ret;
		} else {
			return "";
		}
	}
}
