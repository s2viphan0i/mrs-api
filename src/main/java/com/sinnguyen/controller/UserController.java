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
import com.sinnguyen.model.UserDTO;
import com.sinnguyen.service.UserService;

@RestController
@RequestMapping("")
public class UserController{
	
	public UserController() {
	}
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/users/username/{username}", method=RequestMethod.GET)
    public ResponseModel getUserByUsername(@PathVariable("username") String username) {
        return userService.getByUsername(username);
    }
	
	@RequestMapping(value="/user/users/username/{username}", method=RequestMethod.GET)
    public ResponseModel userGetUserByUsername(@PathVariable("username") String username) {
		SecurityContext context = SecurityContextHolder.getContext();
		String currentUsername = context.getAuthentication().getName();
        return userService.userGetByUsername(username, currentUsername);
    }
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET)
    public ResponseModel getUserById(@PathVariable("id") int id) {
        return userService.getById(id);
    }
	
	@RequestMapping(value="/user/users/{id}", method=RequestMethod.GET)
    public ResponseModel userGetUserById(@PathVariable("id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String currentUsername = context.getAuthentication().getName();
        return userService.userGetById(id, currentUsername);
    }
	
	@RequestMapping(value="/user/users/auth", method=RequestMethod.GET)
    public ResponseModel getUserByAuth() {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
        return userService.getByUsername(username);
    }
	
	@RequestMapping(value="/user", method = RequestMethod.PUT)
	public ResponseModel editUser(@RequestParam(value="file", required=false) MultipartFile file,@RequestParam(value="user") String user) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			User u = mapper.readValue(user, User.class);
			u.setUsername(username);
			return userService.edit(u, file);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	@RequestMapping(value="/user/users/{id}/follows", method=RequestMethod.POST)
    public ResponseModel doFollow(@PathVariable("id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
        return userService.doFollow(username, id);
    }
	
	@RequestMapping(value="/user/users/followings/list", method = RequestMethod.POST)
	public ResponseModel userGetListFollowing(@RequestBody UserDTO searchDto) {
		return userService.userGetListFollowing(searchDto);
	}
	
	@RequestMapping(value="/user/users/list", method = RequestMethod.POST)
	public ResponseModel userGetList(@RequestBody UserDTO searchDto) {
		return userService.userGetList(searchDto);
	}
	
	@RequestMapping(value="/users/list", method = RequestMethod.POST)
	public ResponseModel getList(@RequestBody UserDTO searchDto) {
		return userService.getList(searchDto);
	}
	
}
