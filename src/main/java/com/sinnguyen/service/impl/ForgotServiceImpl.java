package com.sinnguyen.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.ForgotDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ForgotDTO;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;
import com.sinnguyen.service.ForgotService;
import com.sinnguyen.util.Converter;

@Service
public class ForgotServiceImpl implements ForgotService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ForgotDao forgotDao;

	public ResponseModel forgot(UserDTO u) {
		ResponseModel result = new ResponseModel();
		User user = userDao.getUserbyEmail(u.getEmail());
		if (user != null) {
			Forgot forgot = new Forgot();
			forgot.setUser(user);
			forgot.setTimestamp(System.currentTimeMillis() + "");
			forgot.setCode(UUID.randomUUID().toString().substring(0, 5));
			if (forgotDao.add(forgot)) {
				ForgotDTO f = Converter.forgotToForgotDTO(forgot);
				result.setSuccess(true);
				result.setMsg("Gửi mã PIN về email thành công");
				result.setContent(f);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Không tìm thấy người dùng với email " + u.getEmail());
		}
		return result;
	}

	public ResponseModel resetPassword(ForgotDTO forgot) {
		ResponseModel result = new ResponseModel();
		Forgot f = Converter.forgotDTOToForgot(forgot);
		if (forgotDao.checkForgot(f)) {
			if(userDao.changePassword(f.getUser())) {
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
