package com.sinnguyen.service;

import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.UserDTO;

public interface UserService {
	ResponseModel add(UserDTO user);
	ResponseModel delete(User user);
	ResponseModel getAllUser();
	ResponseModel getById(int id);
	
	ResponseModel getByUsername(String username);
	ResponseModel activate(String code);
	ResponseModel editByUsername(UserDTO user);
}
