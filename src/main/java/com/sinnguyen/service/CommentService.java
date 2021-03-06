package com.sinnguyen.service;

import com.sinnguyen.entities.Comment;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SearchDTO;

public interface CommentService {
	ResponseModel getBySongId(int songId, SearchDTO searchDto);
	ResponseModel add(int songId, Comment comment);
	ResponseModel delete(Comment comment);
}
