package com.sinnguyen.service;

import com.sinnguyen.model.ForgotDTO;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;

public interface ForgotService {
	ResponseModel forgot(UserDTO user);
	ResponseModel resetPassword(ForgotDTO forgot);
}
