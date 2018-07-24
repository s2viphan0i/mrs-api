package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Comment;
import com.sinnguyen.model.SearchDTO;

public interface CommentDao {
	List<Comment> getBySongId(int songId, SearchDTO searchDto);
}
