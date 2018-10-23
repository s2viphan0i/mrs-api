package com.sinnguyen.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.ViewDao;
import com.sinnguyen.entities.View;
import com.sinnguyen.model.ViewMapper;
import com.sinnguyen.util.MainUtility;

@Repository
public class ViewDaoImpl implements ViewDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean addView(View view) {
		String sql = "INSERT INTO view(user_id, song_id, timestamp) " + "VALUES (?, ?, ?)";
		try {
			Object[] newObj = new Object[] { view.getUser().getId(), view.getSong().getId(),
					MainUtility.dateToStringFormat(view.getListenTime(), "yyyy-MM-dd HH:mm:ss") };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public View getLastView(String username, int songId) {
		String sql = "SELECT * FROM view INNER JOIN user ON view.user_id = user.id WHERE user.username = ? AND song_id = ?"
				+ " ORDER BY view.id DESC LIMIT 1";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { username, songId },
					new ViewMapper());
			return (View) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean delete(View view) {
		try {
			String sql = "DELETE FROM view WHERE song_id = ?";
			if (this.jdbcTemplate.update(sql, new Object[] {view.getSong().getId()}) > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
