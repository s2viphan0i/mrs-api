package com.sinnguyen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.GenreService;

@Service
public class GenreServiceImpl implements GenreService {

	@Autowired
	GenreDao genreDao;
	
	@Override
	public ResponseModel getAll() {
		ResponseModel result = new ResponseModel();
		List<Genre> genres = genreDao.getAll();
		result.setSuccess(true);
		result.setMsg("Lấy danh sách thể loại thành công");
		result.setContent(genres);
		return result;
	}

}
