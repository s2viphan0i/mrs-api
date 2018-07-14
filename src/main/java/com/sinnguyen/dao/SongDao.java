package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Song;
import com.sinnguyen.model.SongDTO;

public interface SongDao {
	boolean add(Song song);
	List<Song> getList(SongDTO searchDto);
	Song getById(int id);
}
