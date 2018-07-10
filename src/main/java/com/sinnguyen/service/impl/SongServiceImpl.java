package com.sinnguyen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.ResponseModel;
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

}
