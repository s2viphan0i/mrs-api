package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.UserService;
import com.sinnguyen.util.MainUtility;

@RestController
@RequestMapping("")
public class UserController{
	
	public UserController() {
	}
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
    public ResponseModel deleteUser(@RequestBody User user) {
        return userService.delete(user);
    }
	
	@RequestMapping(value="/get-user-by-username", method=RequestMethod.GET)
    public ResponseModel getUserByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }
	
	@RequestMapping(value="/user/get-user-by-username", method=RequestMethod.GET)
    public ResponseModel userGetUserByUsername(@RequestParam String username) {
		SecurityContext context = SecurityContextHolder.getContext();
		String currentUsername = context.getAuthentication().getName();
        return userService.userGetByUsername(username, currentUsername);
    }
	
	@RequestMapping(value="/get-user-by-id", method=RequestMethod.GET)
    public ResponseModel getUserById(@RequestParam int id) {
        return userService.getById(id);
    }
	
	@RequestMapping(value="/user/get-user-by-id", method=RequestMethod.GET)
    public ResponseModel userGetUserById(@RequestParam int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String currentUsername = context.getAuthentication().getName();
        return userService.userGetById(id, currentUsername);
    }
	
	@RequestMapping(value="/user/get-user-by-auth", method=RequestMethod.GET)
    public ResponseModel getUserByAuth() {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
        return userService.getByUsername(username);
    }
	
	@RequestMapping(value="/user/favorite", method=RequestMethod.POST)
    public ResponseModel doFavorite(@RequestParam int songId) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
        return userService.doFavorite(username, songId);
    }
	
	@RequestMapping(value="/user/edit", method = RequestMethod.PUT)
	public ResponseModel editUser(@RequestParam(value="file", required=false) MultipartFile file,@RequestParam(value="user") String user) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			User u = mapper.readValue(user, User.class);
			u.setUsername(username);
			return userService.editByUsername(u, file);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	@RequestMapping(value="/user/follow", method=RequestMethod.POST)
    public ResponseModel doFollow(@RequestParam int userId) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
        return userService.doFollow(username, userId);
    }
	
}
