package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinnguyen.entities.Favorite;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.entities.View;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SongDTO;
import com.sinnguyen.service.SongService;

@CrossOrigin
@RestController
public class SongController {
	
	@Autowired
	private SongService songService;
	
	@RequestMapping(value="/user/songs", method = RequestMethod.POST)
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
	
	@RequestMapping(value="/songs/list", method = RequestMethod.POST)
	public ResponseModel getList(@RequestBody SongDTO searchDto) {
		return songService.getList(searchDto);
	}
	
	@RequestMapping(value="/user/songs/list", method = RequestMethod.POST)
	public ResponseModel userGetList(@RequestBody SongDTO searchDto) {
		return songService.userGetList(searchDto);
	}
	
	@RequestMapping(value="/user/songs/{id}", method = RequestMethod.GET)
	public ResponseModel userGetById(@PathVariable("id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		return songService.userGetById(username, id);
	}
	
	@RequestMapping(value="/songs/{id}", method = RequestMethod.GET)
	public ResponseModel getById(@PathVariable("id") int id) {
		return songService.getById(id);
	}
	
	@RequestMapping(value="/songs/{id}/recommends", method = RequestMethod.GET)
	public ResponseModel getRecommendations(@PathVariable("id") int id) {
		return songService.getRecommendations(id);
	}
	
	@RequestMapping(value="/user/songs/{id}/views", method = RequestMethod.POST)
	public ResponseModel userViewSong(@PathVariable("id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		View view = new View();
		User user = new User();
		user.setUsername(username);
		view.setUser(user);
		Song song = new Song();
		song.setId(id);
		view.setSong(song);
		return songService.userViewSong(view);
	}
	
	@RequestMapping(value="/user/songs/{id}/favorites", method=RequestMethod.POST)
    public ResponseModel doFavorite(@PathVariable("id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		Favorite favorite = new Favorite();
		User user = new User();
		user.setUsername(username);
		favorite.setUser(user);
		Song song = new Song();
		song.setId(id);
		favorite.setSong(song);
        return songService.userFavoriteSong(favorite);
    }
	
	
}
