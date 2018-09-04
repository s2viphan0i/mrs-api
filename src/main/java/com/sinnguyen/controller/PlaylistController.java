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
import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.PlaylistDTO;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.PlaylistService;

@RestController
public class PlaylistController {
	
	@Autowired
	PlaylistService playlistService;
	
	@RequestMapping(value="/user/playlists", method = RequestMethod.POST)
	public ResponseModel addPlaylist(@RequestParam(value="image", required=false) MultipartFile image, @RequestParam(value="playlist") String playlist) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Playlist p = mapper.readValue(playlist, Playlist.class);
			User u = new User();
			u.setUsername(username);
			p.setUser(u);
			return playlistService.add(p, image);
		} catch (Exception ex) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			ex.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value="/user/playlists/list", method = RequestMethod.POST)
	public ResponseModel getListbyAuth() {
		return playlistService.getListbyAuth();
	}
	
	@RequestMapping(value="/playlists/list", method = RequestMethod.POST)
	public ResponseModel getList(@RequestBody PlaylistDTO searchDTO) {
		return playlistService.getList(searchDTO);
	}
	
	@RequestMapping(value="/user/playlists/{id}/songs", method = RequestMethod.POST)
	public ResponseModel addSongtoPlaylist(@RequestBody Song song, @PathVariable("id") int playlistId) {
		return playlistService.addSongtoPlaylist(song, playlistId);
	}
	
	@RequestMapping(value="/user/playlists/{playlistId}/songs/{songId}", method = RequestMethod.DELETE)
	public ResponseModel removeSongFromPlaylist(@PathVariable("playlistId") int playlistId, @PathVariable("songId") int songId) {
		return playlistService.removeSongFromPlaylist(songId, playlistId);
	}
	
	@RequestMapping(value="/playlists/{id}", method = RequestMethod.GET)
	public ResponseModel getById(@PathVariable("id") int id) {
		return playlistService.getById(id);
	}
	
}
