package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.SongDTO;

public interface SongDao {
	boolean add(Song song);
	boolean edit(Song song);
	boolean delete(Song song);
	boolean check(Song song);
	boolean checkOwner(Song song);
	List<Song> getList(SongDTO searchDto);
	List<Song> userGetList(User user, SongDTO searchDto);
	List<Song> userGetFollowingList(User user, SongDTO searchDto);
	List<Song> userGetFavoriteList(User user, SongDTO searchDto);
	List<Song> getSongbyPlaylistId(int playlistId);
	List<Song> getSongDetailbyPlaylistId(int playlistId);
	List<Song> getListRecommendation(int id);
	void getCountList(SongDTO searchDto);
	Song getById(int id);
	Song userGetById(User user, int id);
}
