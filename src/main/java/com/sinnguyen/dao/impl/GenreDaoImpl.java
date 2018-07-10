package com.sinnguyen.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.model.GenreMapper;

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
}
