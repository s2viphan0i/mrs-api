package com.sinnguyen.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.ForgotService;
import com.sinnguyen.service.MailService;
import com.sinnguyen.service.UserService;

@RestController
@RequestMapping("/")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private ForgotService forgotService;

	@Autowired
	private MailService mailService;

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public ResponseModel login() {
		return userService.login();
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseModel register(@RequestBody User user) {
		ResponseModel result = userService.add(user);
		if (result.isSuccess()) {
			mailService.sendWelcomeMail((User) result.getContent());
		}
		return result;
	}

	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public ResponseModel activate(@RequestParam String code) {
		return userService.activate(code);
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseModel forgot(@RequestBody User user) {
		ResponseModel result = forgotService.forgot(user);
		if (result.isSuccess()) {
			Forgot f = (Forgot) result.getContent();
			mailService.sendForgotMail(f);
		}
		result.setContent(null);
		return result;
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ResponseModel resetPasword(@RequestBody Forgot forgot) {
		return forgotService.resetPassword(forgot);
	}
	
	@RequestMapping(value = "/signoff", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
	}
}
