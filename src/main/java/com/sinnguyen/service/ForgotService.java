package com.sinnguyen.service;

import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;

public interface ForgotService {
	ResponseModel forgot(User user);
	ResponseModel resetPassword(Forgot forgot);
}
