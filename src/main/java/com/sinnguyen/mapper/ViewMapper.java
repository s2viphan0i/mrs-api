package com.sinnguyen.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.entities.View;
import com.sinnguyen.util.MainUtility;

public class ViewMapper implements RowMapper<View> {

	public View mapRow(ResultSet rs, int rowNum) throws SQLException {
		View view = new View();
		view.setId(rs.getInt("id"));
		User user = new User();
		user.setId(rs.getInt("user_id"));
		view.setUser(user);
		Song song = new Song();
		song.setId(rs.getInt("song_id"));
		view.setSong(song);
		view.setListenTime(MainUtility.stringtoDate(rs.getString("timestamp"), "yyyy-MM-dd HH:mm:ss"));
		return view;
	}

}
