package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinnguyen.model.ForgotDTO;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;
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

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseModel login(@RequestBody UserDTO user) {
		ResponseModel result = new ResponseModel();
		if(user.getUsername()==null||user.getUsername().equals("")||user.getPassword()==null||user.getPassword().equals("")) {
			result.setSuccess(false);
			result.setMsg("Tên đăng nhập hoặc mật khẩu không được rỗng");
			return result;
		}
		result = userService.getByUsername(user.getUsername());
		if (result.isSuccess()) {
			if (BCrypt.checkpw(user.getPassword(), ((UserDTO) result.getContent()).getPassword())) {
				if (((UserDTO) result.getContent()).isActivated()) {
					result.setSuccess(true);
					result.setMsg("Đăng nhập thành công");
				} else {
					result.setSuccess(false);
					result.setMsg("Tài khoản chưa được kích hoạt");
					result.setContent(null);
				}
			} else {
				result.setSuccess(false);
				result.setMsg("Sai tên đăng nhập hoặc mật khẩu");
				result.setContent(null);
			}
		}
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseModel register(@RequestBody UserDTO user) {
		ResponseModel result = userService.add(user);
		if (result.isSuccess()) {
			mailService.sendWelcomeMail((UserDTO) result.getContent());
		}
		return result;
	}

	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public ResponseModel activate(@RequestParam String code) {
		return userService.activate(code);
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseModel forgot(@RequestBody UserDTO user) {
		ResponseModel result = forgotService.forgot(user);
		if (result.isSuccess()) {
			ForgotDTO f = (ForgotDTO) result.getContent();
			mailService.sendForgotMail(f);
		}
		result.setContent(null);
		return result;
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ResponseModel resetPasword(@RequestBody ForgotDTO forgot) {
		return forgotService.resetPassword(forgot);
	}
}
