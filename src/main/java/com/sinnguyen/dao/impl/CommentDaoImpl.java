package com.sinnguyen.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.CommentDao;
import com.sinnguyen.entities.Comment;
import com.sinnguyen.model.CommentMapper;
import com.sinnguyen.model.SearchDTO;

@Repository
public class CommentDaoImpl implements CommentDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Comment> getBySongId(int songId, SearchDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT comment.*, user.fullname AS owner_name FROM comment INNER JOIN user ON comment.user_id = user.id WHERE comment.song_id = ? "
				+ "ORDER BY create_time LIMIT ? OFFSET ?");
		if(searchDto.getResults()==null) {
			searchDto.setResults(10);
		}
		if(searchDto.getPage()==null) {
			searchDto.setPage(1);
		}
//		sql.append(" LIMIT "+searchDto.getResults()+" OFFSET "+(searchDto.getPage() - 1) * searchDto.getResults());
		try {
			Object[] newObj = new Object[] { songId, searchDto.getResults(), (searchDto.getPage() - 1) * searchDto.getResults() };
			List<Comment> comments = this.jdbcTemplate.query(sql.toString(), newObj, new CommentMapper());
			return comments;
		} catch (Exception e) {
			
		}
		return null;
	}
	
}
