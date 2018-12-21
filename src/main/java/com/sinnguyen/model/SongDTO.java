package com.sinnguyen.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SongDTO extends SearchDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer genreId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date endDate;
	private Integer userId;
	private String fullname;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date viewStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date viewEndDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date favoriteStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date favoriteEndDate;

	public SongDTO() {
		super();
	}

	public Date getViewStartDate() {
		return viewStartDate;
	}

	public void setViewStartDate(Date viewStartDate) {
		this.viewStartDate = viewStartDate;
	}

	public Date getViewEndDate() {
		return viewEndDate;
	}

	public void setViewEndDate(Date viewEndDate) {
		this.viewEndDate = viewEndDate;
	}

	public Date getFavoriteStartDate() {
		return favoriteStartDate;
	}

	public void setFavoriteStartDate(Date favoriteStartDate) {
		this.favoriteStartDate = favoriteStartDate;
	}

	public Date getFavoriteEndDate() {
		return favoriteEndDate;
	}

	public void setFavoriteEndDate(Date favoriteEndDate) {
		this.favoriteEndDate = favoriteEndDate;
	}

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

}
