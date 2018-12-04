package com.sinnguyen.dao;

import java.util.Date;

import com.sinnguyen.entities.Favorite;

public interface FavoriteDao {
	boolean checkFavorite(Favorite favorite);
	boolean addFavorite(Favorite favorite);
	boolean removeFavorite(Favorite favorite);
	boolean delete(Favorite favorite);
	int reportFavorite(Date from, Date to);
}
