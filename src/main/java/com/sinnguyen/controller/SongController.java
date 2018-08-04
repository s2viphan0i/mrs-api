package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.entities.View;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SongDTO;
import com.sinnguyen.service.SongService;
import com.sinnguyen.service.UserService;

@RestController
public class SongController {
	
	@Autowired
	private SongService songService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/user/song/add", method = RequestMethod.POST)
	public ResponseModel addSong(@RequestParam(value="file", required=false) MultipartFile file, 
			@RequestParam(value="image", required=false) MultipartFile image, @RequestParam(value="song") String song) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Song s = mapper.readValue(song, Song.class);
			User u = new User();
			u.setUsername(username);
			s.setUser(u);
			return songService.add(s, file, image);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value="/song/get-list", method = RequestMethod.POST)
	public ResponseModel getList(@RequestBody SongDTO searchDto) {
		return songService.getList(searchDto);
	}
	
	@RequestMapping(value="/user/song/get-list", method = RequestMethod.POST)
	public ResponseModel userGetList(@RequestBody SongDTO searchDto) {
		return songService.userGetList(searchDto);
	}
	
	@RequestMapping(value="/user/song", method = RequestMethod.GET)
	public ResponseModel userGetById(@RequestParam(name="id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		return songService.userGetById(username, id);
	}
	
	@RequestMapping(value="/user/view", method = RequestMethod.POST)
	public ResponseModel userViewSong(@RequestBody View view) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = new User();
		user.setUsername(username);
		view.setUser(user);
		return songService.userViewSong(view);
	}
	
	@RequestMapping(value="/song", method = RequestMethod.GET)
	public ResponseModel getById(@RequestParam(name="id") int id) {
		return songService.getById(id);
	}
}
