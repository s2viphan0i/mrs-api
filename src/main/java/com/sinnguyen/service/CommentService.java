package com.sinnguyen.service;

import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SearchDTO;

public interface CommentService {
	ResponseModel getBySongId(int songId, SearchDTO searchDto);
}
