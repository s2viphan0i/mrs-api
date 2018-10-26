package com.sinnguyen.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.FollowDao;
import com.sinnguyen.entities.Follow;
import com.sinnguyen.util.MainUtility;

@Repository
public class FollowDaoImpl implements FollowDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean checkFollow(Follow follow) {
		String sql = "SELECT EXISTS(SELECT 1 FROM follow WHERE follow.follower_id = ? AND follow.following_id = ?)";
		if (this.jdbcTemplate.queryForObject(sql,
				new Object[] { follow.getFollower().getId(), follow.getFollowing().getId() }, Integer.class) == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addFollow(Follow follow) {
		String sql = "INSERT INTO follow(follower_id, following_id, timestamp) VALUES (?, ?, ?)";
		try {
			Object[] newObj = new Object[] { follow.getFollower().getId(), follow.getFollowing().getId(),
					MainUtility.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss") };
			int row = this.jdbcTemplate.update(sql, newObj);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeFollow(Follow follow) {
		String sql = "DELETE FROM follow WHERE following_id = ? AND follower_id = ?";
		try {
			Object[] newObj = new Object[] { follow.getFollowing().getId(), follow.getFollower().getId() };
			int row = this.jdbcTemplate.update(sql, newObj);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
