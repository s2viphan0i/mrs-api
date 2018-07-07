package com.sinnguyen.dao;

import com.sinnguyen.entities.Forgot;

public interface ForgotDao {
	boolean add(Forgot forgot);
	boolean checkForgot(Forgot forgot);
}
