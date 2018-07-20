package com.sinnguyen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.FavoriteDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.UserService;
import com.sinnguyen.util.MainUtility;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;
	
	@Autowired
	FavoriteDao favoriteDao;

	public ResponseModel add(User user) {
		ResponseModel result = new ResponseModel();
		if (user.getUsername() == null || user.getUsername().equals("") || user.getPassword() == null
				|| user.getPassword().equals("") || user.getFullname() == null || user.getFullname().equals("")
				|| user.getEmail() == null || user.getEmail().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin người dùng không hợp lệ");
		} else {
			if (userDao.checkUsername(user)) {
				if (userDao.add(user) && userDao.insertActivation(user)) {
					user.setId(user.getId());
					user.setCode(user.getCode());
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
		ResponseModel result = new ResponseModel();
		User user = userDao.getUserbyId(id);

		if (user != null) {
			result.setSuccess(true);
			result.setMsg("Lấy thông tin thành công");
			result.setContent(user);
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	public ResponseModel getAllUser() {
		ResponseModel result = new ResponseModel();
		result.setSuccess(true);
		result.setMsg("Lấy danh sách người dùng thành công");
		List<User> users = userDao.getAllUser();

		result.setContent(users);
		return result;
	}
	
	public ResponseModel getByUsername(String username) {
		ResponseModel result = new ResponseModel();
		User user = userDao.getUserbyUsername(username);

		if (user != null) {
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
	public ResponseModel editByUsername(User user, MultipartFile file) {
		ResponseModel result = new ResponseModel();
		if (user.getUsername() == null || user.getUsername().equals("") || user.getFullname() == null 
				|| user.getFullname().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin người dùng không hợp lệ");
		} else {
			if(file!=null) {
				String filename = MainUtility.saveFile(file);
				user.setAvatar(filename);
			}
			if (userDao.editByUsername(user)) {
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
	
	public ResponseModel doFavorite(String username, int songId) {
		ResponseModel result = new ResponseModel();
		try {
			if(favoriteDao.checkFavorite(username, songId)) {
				favoriteDao.removeFavorite(username, songId);
				result.setSuccess(true);
				result.setMsg("Xóa bài hát ưa thích thành công");
			}else {
				favoriteDao.addFavorite(username, songId);
				result.setSuccess(true);
				result.setMsg("Thêm bài hát ưa thích thành công");
			}
		}catch(Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

}
