package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.AdminService;
import com.sinnguyen.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(value="/report/songs", method = RequestMethod.POST)
	public ResponseModel reportUpload(@RequestParam(value="from") String from, @RequestParam(value="to") String to) {
		return adminService.reportUpload(from, to);
	}
	
	@RequestMapping(value="/report/views", method = RequestMethod.POST)
	public ResponseModel reportView(@RequestParam(value="from") String from, @RequestParam(value="to") String to) {
		return adminService.reportView(from, to);
	}
	
	@RequestMapping(value="/report/favorites", method = RequestMethod.POST)
	public ResponseModel reportFavorite(@RequestParam(value="from") String from, @RequestParam(value="to") String to) {
		return adminService.reportFavorite(from, to);
	}
	
	@RequestMapping(value="/report/follows", method = RequestMethod.POST)
	public ResponseModel reportFollow(@RequestParam(value="from") String from, @RequestParam(value="to") String to) {
		return adminService.reportFollow(from, to);
	}
	
	@RequestMapping(value="/report/users", method = RequestMethod.POST)
	public ResponseModel reportUser(@RequestParam(value="from") String from, @RequestParam(value="to") String to) {
		return adminService.reportUser(from, to);
	}
	
	@RequestMapping(value="/songs/{id}", method = RequestMethod.PUT)
	public ResponseModel adminEditSong(@PathVariable("id") int id, @RequestParam(value="image", required=false) MultipartFile image, @RequestParam(value="song") String song) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Song s = mapper.readValue(song, Song.class);
			User u = new User();
			u.setRole("ROLE_ADMIN");
			u.setUsername(username);
			s.setUser(u);
			s.setId(id);
			return adminService.editSong(s, image);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value="/users/{id}", method = RequestMethod.PUT)
	public ResponseModel adminEditUser(@PathVariable("id") int id, @RequestParam(value="file", required=false) MultipartFile file, @RequestParam(value="user") String user) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			User u = mapper.readValue(user, User.class);
			u.setUsername(username);
			u.setId(id);
			return adminService.editUser(u, file);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value="/songs/{id}", method = RequestMethod.DELETE)
	public ResponseModel deleteSong(@PathVariable("id") int id) {
		ResponseModel result = new ResponseModel();
		try {
			Song s = new Song();
			s.setId(id);
			return adminService.deleteSong(s);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value="/users/{id}", method = RequestMethod.DELETE)
	public ResponseModel deleteUser(@PathVariable("id") int id) {
		ResponseModel result = new ResponseModel();
		try {
			User u = new User();
			u.setId(id);
			return adminService.deleteUser(u);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
}
