package com.sinnguyen.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDTO extends SearchDTO {

	private static final long serialVersionUID = 1L;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date followStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date followEndDate;
	private boolean followed;
	
	public UserDTO() {
		super();
	}

	public Date getFollowStartDate() {
		return followStartDate;
	}

	public void setFollowStartDate(Date followStartDate) {
		this.followStartDate = followStartDate;
	}

	public Date getFollowEndDate() {
		return followEndDate;
	}

	public void setFollowEndDate(Date followEndDate) {
		this.followEndDate = followEndDate;
	}

	public boolean getFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

}
