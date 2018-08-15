package com.sinnguyen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.CommentDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Comment;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentDao commentDao;

	@Autowired
	UserDao userDao;

	@Autowired
	SongDao songDao;

	@Override
	public ResponseModel getBySongId(int songId, SearchDTO searchDto) {
		ResponseModel result = new ResponseModel();
		List<Comment> comments = commentDao.getBySongId(songId, searchDto);
		commentDao.getCountList(songId, searchDto);
		if (comments == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (comments.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Bài hát chưa có bình luận");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(searchDto.getTotal());
			result.setContent(comments);
		}
		return result;
	}

	@Override
	public ResponseModel add(int songId, Comment comment) {
		ResponseModel result = new ResponseModel();
		User user = userDao.getUserbyUsername(comment.getUser().getUsername());
		comment.setUser(user);
		Song song = songDao.getById(songId);
		if (song != null) {
			commentDao.addComment(song, comment);
			result.setSuccess(true);
			result.setMsg("Bình luận bài hát thành công");
			result.setContent(comment);

		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

}
