package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.SongDTO;

public interface SongDao {
	boolean add(Song song);
	List<Song> getList(SongDTO searchDto);
	List<Song> userGetList(User user, SongDTO searchDto);
	List<Song> getSongbyPlaylistId(int playlistId);
	void getCountList(SongDTO searchDto);
	Song getById(int id);
	Song userGetById(User user, int id);
}
