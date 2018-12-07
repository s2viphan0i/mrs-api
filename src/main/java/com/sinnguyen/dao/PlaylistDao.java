package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.PlaylistDTO;

public interface PlaylistDao {
	boolean add(Playlist playlist);
	boolean edit(Playlist playlist);
	boolean delete(Playlist playlist);
	boolean deleteAllSong(Playlist playlist);
	boolean checkSonginPLaylist(Song song, Playlist playlistId);
	boolean check(Playlist playlist);
	boolean addSong(Song song, Playlist playlist);
	boolean removeSong(Song song, Playlist playlist);
	Playlist getById(int id);
	List<Playlist> getList(PlaylistDTO searchDto);
	void getCountList(PlaylistDTO searchDto);
}
