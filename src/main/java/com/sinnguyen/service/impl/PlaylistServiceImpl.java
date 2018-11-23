package com.sinnguyen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.PlaylistDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.PlaylistDTO;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.PlaylistService;
import com.sinnguyen.util.MainUtility;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	PlaylistDao playlistDao;
	
	@Autowired
	SongDao songDao;
	
	@Override
	public ResponseModel add(Playlist playlist, MultipartFile image) {
		ResponseModel result = new ResponseModel();
		if (playlist.getTitle() == null || playlist.getTitle().equals("") || image == null || !image.getContentType().matches("image\\/?\\w+")){
			result.setSuccess(false);
			result.setMsg("Thông tin playlist không hợp lệ");
		} else {
			User user = userDao.getUserbyUsername(playlist.getUser().getUsername());
			playlist.setUser(user);
			String imageurl = MainUtility.saveSquareImage(image);
			playlist.setImage(imageurl);
			if (playlistDao.add(playlist)) {
				result.setSuccess(true);
				result.setMsg("Tạo playlist thành công");
				result.setContent(playlist);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			}
		}
		return result;
	}

	@Override
	public ResponseModel getListbyAuth() {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		PlaylistDTO searchDto = new PlaylistDTO();
		searchDto.setUsername(username);
		List<Playlist> playlists = playlistDao.getList(searchDto);
		if (playlists == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (playlists.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Bạn chưa tạo playlist nào");
		} else {
			for(Playlist p: playlists) {
				p.setSongs(songDao.getSongbyPlaylistId(p.getId()));
			}
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(playlists);
		}
		return result;
	}

	@Override
	public ResponseModel addSongtoPlaylist(Song song, int playlistId) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		Playlist playlist = new Playlist();
		playlist.setId(playlistId);
		User user = new User();
		user.setUsername(username);
		playlist.setUser(user);
		if(!playlistDao.check(playlist)||!songDao.check(song)) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		else if(!playlistDao.checkSonginPLaylist(song, playlist)) {
			if(playlistDao.addSong(song, playlist)) {
				result.setSuccess(true);
				result.setMsg("Thêm bài hát vào playlist thành công");
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Bài hát đã tồn tại trong playlist");
		}
		return result;
	}

	@Override
	public ResponseModel removeSongFromPlaylist(int songId, int playlistId) {
		ResponseModel result = new ResponseModel();
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		Playlist playlist = new Playlist();
		playlist.setId(playlistId);
		User user = new User();
		user.setUsername(username);
		playlist.setUser(user);
		Song song = new Song();
		song.setId(songId);
		if(!playlistDao.check(playlist)||!songDao.check(song)) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		else if(playlistDao.checkSonginPLaylist(song, playlist)) {
			if(playlistDao.removeSong(song, playlist)) {
				result.setSuccess(true);
				result.setMsg("Xóa bài hát trong playlist thành công");
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Bài hát không tồn tại trong playlist");
		}
		return result;
	}

	@Override
	public ResponseModel getById(int id) {
		ResponseModel result = new ResponseModel();
		try {
			Playlist playlist = playlistDao.getById(id);
			if (playlist != null) {
				playlist.setSongs(songDao.getSongDetailbyPlaylistId(id));
				result.setSuccess(true);
				result.setMsg("Lấy thông tin playlist thành công");
				result.setContent(playlist);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel getList(PlaylistDTO searchDto) {
		ResponseModel result = new ResponseModel();
		List<Playlist> playlists = playlistDao.getList(searchDto);
		playlistDao.getCountList(searchDto);
		if (playlists == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (playlists.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm thấy playlist nào");
		} else {
			for(Playlist p: playlists) {
				p.setSongs(songDao.getSongbyPlaylistId(p.getId()));
			}
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(playlists);
			result.setTotal(searchDto.getTotal());
		}
		return result;
	}

}
