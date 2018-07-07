package com.sinnguyen.service;

import com.sinnguyen.model.ForgotDTO;
import com.sinnguyen.model.UserDTO;

public interface MailService {
	void sendWelcomeMail(UserDTO userDTO);
	void sendForgotMail(ForgotDTO forgotDTO);
}
