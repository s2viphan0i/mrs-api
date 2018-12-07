package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.PlaylistDTO;
import com.sinnguyen.model.ResponseModel;

public interface PlaylistService {
	ResponseModel add(Playlist playlist, MultipartFile image);
	ResponseModel edit(Playlist playlist, MultipartFile image);
	ResponseModel delete(Playlist playlist);
	ResponseModel getListbyAuth();
	ResponseModel getList(PlaylistDTO searchDto);
	ResponseModel addSongtoPlaylist(Song song, int playlistId);
	ResponseModel removeSongFromPlaylist(int songId, int playlistId);
	ResponseModel getById(int id);
}
