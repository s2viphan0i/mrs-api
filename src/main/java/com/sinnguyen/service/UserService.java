package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;

public interface UserService {
	ResponseModel add(User user);
	ResponseModel delete(User user);
	ResponseModel getAllUser();
	
	ResponseModel login();
	ResponseModel getById(int id);
	ResponseModel userGetById(int id, String currentUsername);
	ResponseModel getByUsername(String username);
	ResponseModel userGetByUsername(String username, String currentUsername);
	ResponseModel activate(String code);
	ResponseModel editByUsername(User user, MultipartFile file);
	ResponseModel doFollow(String username, int userId);
	ResponseModel userGetListFollowing(UserDTO searchDto);
	ResponseModel userGetList(UserDTO searchDto);
	ResponseModel getList(UserDTO searchDto);
}
