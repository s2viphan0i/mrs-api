package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.SongService;
import com.sinnguyen.service.UserService;
import com.sinnguyen.util.MainUtility;

@RestController
@RequestMapping("/user/song")
public class SongController {
	
	@Autowired
	private SongService songService;
	
	@RequestMapping(value="/add", method = RequestMethod.POST)
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
}
