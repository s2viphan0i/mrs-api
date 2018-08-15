package com.sinnguyen.dao;

import com.sinnguyen.entities.Favorite;

public interface FavoriteDao {
	boolean checkFavorite(Favorite favorite);
	boolean addFavorite(Favorite favorite);
	boolean removeFavorite(Favorite favorite);
}
