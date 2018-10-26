package com.sinnguyen.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.ForgotService;
import com.sinnguyen.service.MailService;
import com.sinnguyen.service.UserService;

import redis.clients.jedis.Jedis;

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
	
	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void logout(@RequestParam String token) {
//		System.out.println(token);
//		Jedis jedis = new Jedis("localhost");
//		jedis.del(token);
//		jedis.close();
	}
}
