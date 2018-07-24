package com.sinnguyen.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.FavoriteDao;
import com.sinnguyen.util.MainUtility;

@Repository
public class FavoriteDaoImpl implements FavoriteDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public boolean checkFavorite(String username, int songId) {
		String sql = "SELECT EXISTS(SELECT 1 FROM favorite INNER JOIN user ON user.id = favorite.user_id "
				+ "WHERE user.username = ? AND favorite.song_id = ?)";
		if (this.jdbcTemplate.queryForObject(sql, new Object[] { username, songId }, Integer.class) == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addFavorite(String username, int songId) {
		String sql = "INSERT INTO favorite(user_id, song_id, timestamp) "
				+ "VALUES ((SELECT id FROM user WHERE user.username = ?), ?, ?)";
		try {
		Object[] newObj = new Object[] { username, songId, MainUtility.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss") };
		int row = this.jdbcTemplate.update(sql, newObj);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeFavorite(String username, int songId) {
		String sql = "DELETE FROM favorite WHERE song_id = ? AND user_id = (SELECT id FROM user WHERE username = ?)";
		try {
			Object[] newObj = new Object[] {songId, username };
			int row = this.jdbcTemplate.update(sql, newObj);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
