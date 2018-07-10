package com.sinnguyen.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sinnguyen.entities.Genre;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;

public class SongMapper  implements RowMapper<Song> {

	public Song mapRow(ResultSet rs, int rowNum) throws SQLException {
		Song song = new Song();
		Genre genre = new Genre();
		song.setId(rs.getInt("id"));
		genre.setId(rs.getInt("genre_id"));
		song.setGenre(genre);
		User owner = new User();
		owner.setId(rs.getInt("owner_id"));
		song.setUser(owner);
		song.setTitle(rs.getString("title"));
		song.setUrl(rs.getString("url"));
		song.setLyric(rs.getString("lyric"));
		song.setMode(rs.getBoolean("mode"));
		song.setImage(rs.getString("image"));
		song.setCreateTime(rs.getDate("createTime"));
		song.setTotalTime(rs.getInt("totalTime"));
		song.setViews(rs.getInt("views"));
		song.setFavorites(rs.getInt("favorites"));
		return song;
	}
}