package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;

public interface UserService {
	ResponseModel add(User user);
	ResponseModel delete(User user);
	ResponseModel getAllUser();
	
	ResponseModel getById(int id);
	ResponseModel userGetById(int id, String currentUsername);
	ResponseModel getByUsername(String username);
	ResponseModel userGetByUsername(String username, String currentUsername);
	ResponseModel activate(String code);
	ResponseModel editByUsername(User user, MultipartFile file);
	ResponseModel doFavorite(String username, int songid);
	ResponseModel doFollow(String username, int userId);
}
