package com.sinnguyen.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.FavoriteDao;
import com.sinnguyen.entities.Favorite;
import com.sinnguyen.util.MainUtility;

@Repository
public class FavoriteDaoImpl implements FavoriteDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public boolean checkFavorite(Favorite favorite) {
		String sql = "SELECT EXISTS(SELECT 1 FROM favorite WHERE favorite.user_id = ? AND favorite.song_id = ?)";
		if (this.jdbcTemplate.queryForObject(sql, new Object[] { favorite.getUser().getId(), favorite.getSong().getId() }, Integer.class) == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addFavorite(Favorite favorite) {
		String sql = "INSERT INTO favorite(user_id, song_id, timestamp) VALUES (?, ?, ?)";
		try {
		Object[] newObj = new Object[] { favorite.getUser().getId(), favorite.getSong().getId(), MainUtility.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss") };
		int row = this.jdbcTemplate.update(sql, newObj);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeFavorite(Favorite favorite) {
		String sql = "DELETE FROM favorite WHERE song_id = ? AND user_id = ?";
		try {
			Object[] newObj = new Object[] {favorite.getSong().getId(), favorite.getUser().getId()};
			int row = this.jdbcTemplate.update(sql, newObj);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean delete(Favorite favorite) {
		try {
			String sql = "DELETE FROM favorite WHERE song_id = ?";
			if (this.jdbcTemplate.update(sql, new Object[] {favorite.getSong().getId()}) > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
