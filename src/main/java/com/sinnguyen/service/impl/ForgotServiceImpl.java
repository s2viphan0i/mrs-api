package com.sinnguyen.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.ForgotDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.ForgotService;

@Service
public class ForgotServiceImpl implements ForgotService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ForgotDao forgotDao;

	public ResponseModel forgot(User user) {
		ResponseModel result = new ResponseModel();
		user = userDao.getUserbyEmail(user.getEmail());
		if (user != null) {
			Forgot forgot = new Forgot();
			forgot.setUser(user);
			forgot.setTimestamp(System.currentTimeMillis() + "");
			forgot.setCode(UUID.randomUUID().toString().substring(0, 5));
			if (forgotDao.add(forgot)) {
				result.setSuccess(true);
				result.setMsg("Gửi mã PIN về email thành công");
				result.setContent(forgot);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Không tìm thấy người dùng với email " + user.getEmail());
		}
		return result;
	}

	public ResponseModel resetPassword(Forgot forgot) {
		ResponseModel result = new ResponseModel();
		if (forgotDao.checkForgot(forgot)) {
			if(userDao.changePassword(forgot.getUser())) {
				result.setSuccess(true);
				result.setMsg("Đổi mật khẩu thành công");
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Mã code đã hết hạn hoặc không tồn tại! Vui lòng thử lại");
		}
		return result;
	}
}
