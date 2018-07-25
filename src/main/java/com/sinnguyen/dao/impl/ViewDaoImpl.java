package com.sinnguyen.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.ViewDao;
import com.sinnguyen.entities.View;
import com.sinnguyen.util.MainUtility;

@Repository
public class ViewDaoImpl implements ViewDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean addView(View view) {
		String sql = "INSERT INTO view(user_id, song_id, timestamp) "
				+ "VALUES (?, ?, ?)";
		try {
		Object[] newObj = new Object[] { view.getUser().getId(), view.getSong().getId(), MainUtility.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss") };
		int row = this.jdbcTemplate.update(sql, newObj);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
