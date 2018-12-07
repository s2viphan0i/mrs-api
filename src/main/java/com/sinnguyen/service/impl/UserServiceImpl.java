package com.sinnguyen.service.impl;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.FavoriteDao;
import com.sinnguyen.dao.FollowDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Follow;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;
import com.sinnguyen.service.UserService;
import com.sinnguyen.util.MainUtility;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	@Autowired
	SongDao songDao;

	@Autowired
	FavoriteDao favoriteDao;

	@Autowired
	FollowDao followDao;
	
	@Autowired
	DataSource dataSource;
	
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

	public ResponseModel login() {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User u = userDao.getUserbyUsername(username);
		result.setSuccess(true);
		result.setMsg("Đăng nhập thành công");
		result.setContent(u);
		
		return result;
	}
	
	@Override
	public ResponseModel edit(User user, MultipartFile file) {
		ResponseModel result = new ResponseModel();
		if (user.getUsername() == null || user.getUsername().equals("") || user.getFullname() == null
				|| user.getFullname().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin người dùng không hợp lệ");
		} else {
			if (file != null) {
				String filename = MainUtility.saveSquareImage(file);
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

	public ResponseModel doFollow(String username, int userId) {
		ResponseModel result = new ResponseModel();
		Follow follow = new Follow();
		User follower = userDao.getUserbyUsername(username);
		follow.setFollower(follower);
		User following = userDao.getUserbyId(userId);
		follow.setFollowing(following);
		if (following != null) {
			if (follower.getId() == following.getId()) {
				result.setSuccess(false);
				result.setMsg("Bạn không thể theo dõi chính mình");
			} else if (followDao.checkFollow(follow)) {
				followDao.removeFollow(follow);
				result.setSuccess(true);
				result.setMsg("Bỏ theo dõi thành công");
			} else {
				followDao.addFollow(follow);
				result.setSuccess(true);
				result.setMsg("Theo dõi thành công");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel userGetById(int id, String currentUsername) {
		ResponseModel result = new ResponseModel();
		User currentUser = userDao.getUserbyUsername(currentUsername);
		User user = userDao.userGetUserbyId(id, currentUser.getId());

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
	public ResponseModel userGetByUsername(String username, String currentUsername) {
		ResponseModel result = new ResponseModel();
		User currentUser = userDao.getUserbyUsername(currentUsername);
		User user = userDao.userGetUserbyUsername(username, currentUser.getId());

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
	public ResponseModel userGetList(UserDTO searchDto) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userDao.getUserbyUsername(username);
		ResponseModel result = new ResponseModel();
		List<User> users = userDao.userGetList(user, searchDto);
		userDao.getCountList(searchDto);
		if(users==null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if(users.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được người dùng phù hợp");
			result.setTotal(0);
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(searchDto.getTotal());
			result.setContent(users);
		}
		return result;
	}

	@Override
	public ResponseModel getList(UserDTO searchDto) {
		ResponseModel result = new ResponseModel();
		List<User> users = userDao.getList(searchDto);
		userDao.getCountList(searchDto);
		if(users==null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if(users.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được người dùng phù hợp");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(searchDto.getTotal());
			result.setContent(users);
		}
		return result;
	}

	@Override
	public ResponseModel userGetListFollowing(UserDTO searchDto) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userDao.getUserbyUsername(username);
		ResponseModel result = new ResponseModel();
		List<User> users = userDao.userGetListFollowing(user, searchDto);
		userDao.getCountListFollowing(user, searchDto);
		if(users==null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if(users.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được người dùng phù hợp");
			result.setTotal(0);
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(searchDto.getTotal());
			result.setContent(users);
		}
		return result;
	}

}
