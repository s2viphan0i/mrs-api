package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.GenreService;

@RestController
@RequestMapping("/user/genre")
public class GenreController {
	
	@Autowired
	GenreService genreService;
	
	@RequestMapping(value="/genres", method = RequestMethod.GET)
	public ResponseModel getAllGenres() {
		return genreService.getAll();
	}
}
