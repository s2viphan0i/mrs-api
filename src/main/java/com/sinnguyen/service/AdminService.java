package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;

public interface AdminService {
	ResponseModel editSong(Song song, MultipartFile image);
	ResponseModel deleteSong(Song song);
	ResponseModel editUser(User user, MultipartFile file);
	ResponseModel deleteUser(User user);
	
	ResponseModel reportUpload(String from, String to);
	ResponseModel reportView(String from, String to);
	ResponseModel reportFavorite(String from, String to);
	
	ResponseModel reportFollow(String from, String to);
	ResponseModel reportUser(String from, String to);
}
