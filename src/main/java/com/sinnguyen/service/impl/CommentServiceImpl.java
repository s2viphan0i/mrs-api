package com.sinnguyen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.CommentDao;
import com.sinnguyen.entities.Comment;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentDao commentDao;
	
	@Override
	public ResponseModel getBySongId(int songId, SearchDTO searchDto) {
		ResponseModel result = new ResponseModel();
		List<Comment> comments = commentDao.getBySongId(songId, searchDto);
		if(comments==null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if(comments.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không có bình luận");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(comments);
		}
		return result;
	}

}
