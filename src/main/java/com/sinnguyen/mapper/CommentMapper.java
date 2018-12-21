package com.sinnguyen.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sinnguyen.entities.Comment;
import com.sinnguyen.entities.User;
import com.sinnguyen.util.MainUtility;

public class CommentMapper implements RowMapper<Comment> {

	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException{
		Comment comment = new Comment();
		comment.setId(rs.getInt("id"));
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setFullname(rs.getString("owner_fullname"));
		user.setUsername(rs.getString("owner_username"));
		user.setAvatar(rs.getString("owner_avatar"));
		comment.setUser(user);
		comment.setContent(rs.getString("content"));
		comment.setCreateTime(MainUtility.stringtoDate(rs.getString("create_time"), "yyyy-MM-dd HH:mm:ss"));
		return comment;
	}

}
