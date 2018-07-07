package com.sinnguyen.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.ForgotDao;
import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.UserMapper;

@Repository
public class ForgotDaoImpl implements ForgotDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean add(Forgot forgot) {
		try {
			String sql = "INSERT INTO forgot (user_id, code, timestamp) VALUES (?,?,?)";

			Object[] newObj = new Object[] { forgot.getUser().getId(), forgot.getCode(), forgot.getTimestamp() };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception ex) {

		}
		return false;
	}

	public boolean checkForgot(Forgot forgot) {
		try {
			String sql = "SELECT user.* FROM forgot INNER JOIN user WHERE forgot.user_id = user.id "
					+ "AND user.email = ? AND forgot.code = ? AND timestamp BETWEEN ? AND ?";
			long timestamp = System.currentTimeMillis();
			Object[] newObj = new Object[] { forgot.getUser().getEmail(), forgot.getCode(), timestamp-600000, timestamp };
			User u = (User)this.jdbcTemplate.queryForObject(sql, newObj, new UserMapper());
			if (u!=null) {
				forgot.getUser().setId(u.getId());
				return true;
			}
		} catch(Exception ex) {
			
		}
		return false;
	}
}
