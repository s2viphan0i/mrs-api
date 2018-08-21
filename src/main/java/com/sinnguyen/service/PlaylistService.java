package com.sinnguyen.service;

import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.ResponseModel;

public interface PlaylistService {
	ResponseModel add(Playlist playlist, MultipartFile image);
	ResponseModel getListbyAuth();
	ResponseModel addSongtoPlaylist(Song song, int playlistId);
}
