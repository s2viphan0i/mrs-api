package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Comment;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.SearchDTO;

public interface CommentDao {
	List<Comment> getBySongId(int songId, SearchDTO searchDto);
	boolean add(Song song, Comment comment);
	void getCountList(int songId, SearchDTO searchDto);
	boolean delete(Comment comment);
	boolean deleteAllBySong(int songId);
}
