package com.sinnguyen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sinnguyen.entities.Comment;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.service.CommentService;

@RestController
@RequestMapping("/")
public class CommentController {

	@Autowired
	CommentService commentService;

	@RequestMapping(value = "/songs/{id}/comments/list", method = RequestMethod.POST)
	public ResponseModel getBySongId(@PathVariable("id") int id, @RequestBody SearchDTO searchDto) {
		return commentService.getBySongId(id, searchDto);
	}

	@RequestMapping(value = "/user/comments/{id}", method = RequestMethod.DELETE)
	public ResponseModel deleteComment(@PathVariable("id") int id) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = new User(); 
		user.setUsername(username);
		Comment comment = new Comment();
		comment.setId(id);
		comment.setUser(user);
		return commentService.delete(comment);
	}
	
	@RequestMapping(value = "/user/songs/{id}/comments", method = RequestMethod.POST)
	public ResponseModel userViewSong(@PathVariable("id") int id, @RequestBody Comment comment) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = new User(); 
		user.setUsername(username);
		comment.setUser(user);
		return commentService.add(id, comment);
	}
}
