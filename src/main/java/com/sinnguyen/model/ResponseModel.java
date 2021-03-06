package com.sinnguyen.model;

import java.io.Serializable;

public class ResponseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean success;
	private String msg;
	private int total;
	private Object content;

	public ResponseModel(boolean success, String msg, Object content) {
		super();
		this.success = success;
		this.msg = msg;
		this.content = content;
	}

	public ResponseModel() {
		super();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
