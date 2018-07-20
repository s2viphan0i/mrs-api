package com.sinnguyen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.model.SongDTO;
import com.sinnguyen.service.SongService;
import com.sinnguyen.util.MainUtility;

@Service
public class SongServiceImpl implements SongService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SongDao songDao;
	
	@Autowired
	private GenreDao genreDao;
	
	@Override
	public ResponseModel add(Song song, MultipartFile file, MultipartFile image) {
		ResponseModel result = new ResponseModel();
		if (song.getGenre() == null || song.getGenre().getId()==0 || song.getTitle() == null
				|| song.getTitle().equals("") || file == null || !file.getContentType().matches("audio\\/?\\w+") 
				|| image ==null || !image.getContentType().matches("image\\/?\\w+")) {
			result.setSuccess(false);
			result.setMsg("Thông tin bài hát không hợp lệ");
		} else {
			Genre genre = genreDao.getById(song.getGenre().getId());
			if(genre == null) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			song.setGenre(genre);
			String fileurl = MainUtility.saveFile(file);
			song.setUrl(fileurl);
			String imageurl = MainUtility.saveFile(image);
			song.setImage(imageurl);
			song.setUser(userDao.getUserbyUsername(song.getUser().getUsername()));
			if (songDao.add(song)) {
				result.setSuccess(true);
				result.setMsg("Upload nhạc thành công");
				result.setContent(song);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			}
		}
		return result;
	}

	@Override
	public ResponseModel getList(SongDTO searchDto) {
		ResponseModel result = new ResponseModel();
		List<Song> songs = songDao.getList(searchDto);
		if(songs==null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if(songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(songs);
		}
		return result;
	}
	
	@Override
	public ResponseModel userGetList(SongDTO searchDto) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userDao.getUserbyUsername(username);
		ResponseModel result = new ResponseModel();
		List<Song> songs = songDao.userGetList(user, searchDto);
		if(songs==null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if(songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(songs);
		}
		return result;
	}
	
	@Override
	public ResponseModel getById(int id) {
		ResponseModel result = new ResponseModel();
		Song song = songDao.getById(id);
		if(song!=null) {
			result.setSuccess(true);
			result.setMsg("Lấy thông tin bài hát thành công");
			result.setContent(song);
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

}
