package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.PlaylistDTO;

public interface PlaylistDao {
	boolean add(Playlist playlist);
	boolean checkSong(int songId, int playlistId);
	boolean addSong(Song song, Playlist playlist);
	List<Playlist> getList(PlaylistDTO searchDto);
}
