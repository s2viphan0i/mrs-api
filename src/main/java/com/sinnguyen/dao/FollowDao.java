package com.sinnguyen.dao;

import java.util.Date;

import com.sinnguyen.entities.Follow;

public interface FollowDao {
	boolean checkFollow(Follow follow);
	boolean addFollow(Follow follow);
	boolean removeFollow(Follow follow);
	int reportFollow(Date from, Date to);
}
