package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.Favorite;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.View;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SongDTO;

public interface SongService {
	ResponseModel add(Song song, MultipartFile file, MultipartFile image);
	ResponseModel edit(Song song, MultipartFile image);
	ResponseModel delete(Song song);
	ResponseModel getList(SongDTO searchDto);
	ResponseModel userGetList(SongDTO searchDto);
	ResponseModel userGetFollowingList(SongDTO songDto);
	ResponseModel userGetFavoriteList(SongDTO songDto);
	ResponseModel userGetById(String username, int id);
	ResponseModel getById(int id);
	ResponseModel userViewSong(View view);
	ResponseModel userFavoriteSong(Favorite favorite);
	ResponseModel getRecommendations(int id);
}
