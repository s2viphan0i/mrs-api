package com.sinnguyen.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.User;

public class PlaylistMapper implements RowMapper<Playlist> {

	public Playlist mapRow(ResultSet rs, int rowNum) throws SQLException {
		Playlist playlist = new Playlist();
		playlist.setId(rs.getInt("id"));
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setUsername(rs.getString("username"));
		user.setFullname(rs.getString("fullname"));
		playlist.setUser(user);
		playlist.setTitle(rs.getString("title"));
		playlist.setImage(rs.getString("image"));
		return playlist;
	}

}
