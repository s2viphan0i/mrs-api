package com.sinnguyen.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.sinnguyen.entities.Genre;

public class GenreMapper  implements RowMapper<Genre> {

	public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
		Genre genre = new Genre();
		genre.setId(rs.getInt("id"));
		genre.setName(rs.getString("name"));
		genre.setNote(rs.getString("note"));
		return genre;
	}
}
