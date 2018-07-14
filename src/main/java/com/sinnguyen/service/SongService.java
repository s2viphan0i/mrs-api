package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.Song;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SongDTO;

public interface SongService {
	ResponseModel add(Song song, MultipartFile file, MultipartFile image);
	ResponseModel getList(SongDTO searchDto);
	ResponseModel getById(int id);
}
