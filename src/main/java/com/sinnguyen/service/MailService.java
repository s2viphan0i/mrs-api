package com.sinnguyen.service;

import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;

public interface MailService {
	void sendWelcomeMail(User user);
	void sendForgotMail(Forgot forgot);
}
