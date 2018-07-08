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
import com.sinnguyen.util.MainUtility;

@RestController
@RequestMapping("/user")
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
	
	@RequestMapping(value="/get-user-by-auth", method=RequestMethod.GET)
    public ResponseModel getUserByAuth() {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
        return userService.getByUsername(username);
    }
	
	@RequestMapping(value="/edit", method = RequestMethod.PUT)
	public ResponseModel editUser(@RequestParam(value="file", required=false) MultipartFile file,@RequestParam(value="user") String user) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserDTO u = mapper.readValue(user, UserDTO.class);
			u.setUsername(username);
			if(file!=null) {
				String filename = MainUtility.saveFile(file);
				u.setAvatar(filename);
			}
			return userService.editByUsername(u);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseModel getUser(@PathVariable("id") int id) {
		return userService.getById(id);
	}
}
