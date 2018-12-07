package com.sinnguyen.dao;

import java.util.Date;
import java.util.List;

import com.sinnguyen.entities.User;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.model.UserDTO;

public interface UserDao {
	boolean add(User user);
	boolean checkUsername(User user);
	List<User> getAllUser();
	
	boolean edit(User user);
	boolean editByUsername(User user);
	boolean insertActivation(User user);
	boolean activate(String code);
	User getUserbyEmail(String email);
	User getUserbyUsername(String username);
	User userGetUserbyUsername(String username, int currentId);
	User getUserbyId(int id);
	User userGetUserbyId(int id, int currentId);
	boolean changePassword(User user);
	List<User> userGetListFollowing(User user, UserDTO userDto);
	List<User> userGetList(User user, UserDTO userDto);
	List<User> getList(UserDTO userDto);
	void getCountListFollowing(User user, UserDTO searchDto);
	void getCountList(UserDTO userDto);
	int reportUser(Date from, Date to);
	boolean deactivated(User user);
}
