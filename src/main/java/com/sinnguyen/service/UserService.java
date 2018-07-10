package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;

public interface UserService {
	ResponseModel add(User user);
	ResponseModel delete(User user);
	ResponseModel getAllUser();
	ResponseModel getById(int id);
	
	ResponseModel getByUsername(String username);
	ResponseModel activate(String code);
	ResponseModel editByUsername(User user, MultipartFile file);
}
