package com.sinnguyen.dao;

public interface FavoriteDao {
	boolean checkFavorite(String username, int songId);
	boolean addFavorite(String username, int songId);
	boolean removeFavorite(String username, int songId);
}
