package com.sinnguyen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;
import com.sinnguyen.service.UserService;
import com.sinnguyen.util.Converter;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	public ResponseModel add(UserDTO user) {
		ResponseModel result = new ResponseModel();
		if (user.getUsername() == null || user.getUsername().equals("") || user.getPassword() == null
				|| user.getPassword().equals("") || user.getFullname() == null || user.getFullname().equals("")
				|| user.getEmail() == null || user.getEmail().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin người dùng không hợp lệ");
		} else {
			User u = Converter.userDTOToUser(user);
			if (userDao.checkUsername(u)) {
				if (userDao.add(u) && userDao.insertActivation(u)) {
					user.setId(u.getId());
					user.setCode(u.getCode());
					result.setSuccess(true);
					result.setMsg("Đăng kí thành công, vui lòng kiểm tra mail để kích hoạt tài khoản");
					result.setContent(user);
				} else {
					result.setSuccess(false);
					result.setMsg("Có lỗi xảy ra! Vui lòng kiểm tra lại");
				}
			} else {
				result.setSuccess(false);
				result.setMsg("Tên đăng nhập hoặc email đã tồn tại");
			}
		}
		return result;
	}

	public ResponseModel activate(String code) {
		ResponseModel result = new ResponseModel();
		if (userDao.activate(code)) {
			result.setSuccess(true);
			result.setMsg("Kích hoạt tài khoản thành công! Vui lòng đăng nhập");
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
		}
		return result;
	}

	public ResponseModel delete(User user) {
		return null;

	}

	public ResponseModel getById(int id) {
		return null;
	}

	public ResponseModel getAllUser() {
		ResponseModel result = new ResponseModel();
		result.setSuccess(true);
		result.setMsg("Lấy danh sách người dùng thành công");
		List<User> users = userDao.getAllUser();
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for(User u : users) {
			userDTOs.add(Converter.userToUserDTO(u));
		}
		result.setContent(userDTOs);
		return result;
	}
	
	public ResponseModel getByUsername(String username) {
		ResponseModel result = new ResponseModel();
		User u = userDao.getUserbyUsername(username);

		if (u != null) {
			UserDTO user = Converter.userToUserDTO(u);
			result.setSuccess(true);
			result.setMsg("Lấy thông tin thành công");
			result.setContent(user);
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel editByUsername(UserDTO user) {
		ResponseModel result = new ResponseModel();
		if (user.getUsername() == null || user.getUsername().equals("") || user.getFullname() == null || user.getFullname().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin người dùng không hợp lệ");
		} else {
			User u = Converter.userDTOToUser(user);
			if (userDao.editByUsername(u)) {
				result.setSuccess(true);
				result.setMsg("Sửa thông tin người dùng thành công");
				result.setContent(user);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng kiểm tra lại");
			}
		}
		return result;
	}

}
