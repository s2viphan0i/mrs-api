package com.sinnguyen.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.entities.User;
import com.sinnguyen.mapper.GenreMapper;
import com.sinnguyen.mapper.UserMapper;

@Repository
public class GenreDaoImpl implements GenreDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Genre getById(int id) {
		String sql = "SELECT * FROM genre WHERE id = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { id }, new GenreMapper());
			return (Genre) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Genre> getAll() {
		String sql = "SELECT * FROM genre";
		try {
			List<Genre> genres = this.jdbcTemplate.query(sql, new GenreMapper());
			return genres;
		} catch (Exception e) {
			return null;
		}
	}
}
