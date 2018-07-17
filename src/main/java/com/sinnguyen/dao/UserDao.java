package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.User;
import com.sinnguyen.model.SearchDTO;

public interface UserDao {
	boolean add(User user);
	boolean checkUsername(User user);
	boolean delete(User user);
	List<User> getAllUser();
	boolean getById(int id);
	boolean search(SearchDTO searchDTO);
	
	boolean editByUsername(User user);
	boolean insertActivation(User user);
	boolean activate(String code);
	User getUserbyEmail(String email);
	User getUserbyUsername(String username);
	User getUserbyId(int id);
	boolean changePassword(User user);
}
