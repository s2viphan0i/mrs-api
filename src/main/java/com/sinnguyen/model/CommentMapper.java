package com.sinnguyen.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sinnguyen.entities.Comment;
import com.sinnguyen.entities.User;

public class CommentMapper implements RowMapper<Comment> {

	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException{
		Comment comment = new Comment();
		comment.setId(rs.getInt("id"));
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setFullname(rs.getString("owner_name"));
		comment.setUser(user);
		comment.setContent(rs.getString("content"));
		comment.setCreateTime(rs.getDate("create_time"));
		return comment;
	}

}
