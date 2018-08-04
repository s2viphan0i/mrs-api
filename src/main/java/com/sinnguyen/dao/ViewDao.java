package com.sinnguyen.dao;

import com.sinnguyen.entities.View;

public interface ViewDao {
	boolean addView(View view);
	View getLastView(String username, int songId);
}
