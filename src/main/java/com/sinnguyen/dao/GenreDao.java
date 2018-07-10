package com.sinnguyen.dao;

import java.util.List;

import com.sinnguyen.entities.Genre;

public interface GenreDao {
	Genre getById(int id);
	List<Genre> getAll();
}
