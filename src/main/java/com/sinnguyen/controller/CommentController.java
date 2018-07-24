package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.service.CommentService;

@RestController
@RequestMapping("/")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value = "/comment/get-list", method = RequestMethod.POST)
	public ResponseModel getBySongId(@RequestParam int songId, @RequestBody SearchDTO searchDto) {
		return commentService.getBySongId(songId, searchDto);
	}
}
