package com.sinnguyen.entities;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Favorite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private User user;
	private Song song;
	private Date favoriteTime;
	public Favorite() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Song getSong() {
		return song;
	}
	public void setSong(Song song) {
		this.song = song;
	}
	public Date getFavoriteTime() {
		return favoriteTime;
	}
	public void setFavoriteTime(Date favoriteTime) {
		this.favoriteTime = favoriteTime;
	}
}
