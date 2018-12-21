package com.sinnguyen.dao;

import java.util.Date;

import com.sinnguyen.entities.View;

public interface ViewDao {
	boolean addView(View view);
	boolean deleteAllBySong(int songId);
	View getLastView(String username, int songId);
	int reportView(Date from, Date to);
}
