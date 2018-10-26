package com.sinnguyen.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.FavoriteDao;
import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.dao.ViewDao;
import com.sinnguyen.entities.Favorite;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.entities.View;
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

	@Autowired
	private ViewDao viewDao;

	@Autowired
	private FavoriteDao favoriteDao;

	@Override
	public ResponseModel add(Song song, MultipartFile file, MultipartFile image) {
		ResponseModel result = new ResponseModel();
		if (song.getGenre() == null || song.getGenre().getId() == 0 || song.getTitle() == null
				|| song.getTitle().equals("") || file == null || !file.getContentType().matches("audio\\/?\\w+")
				|| image == null || !image.getContentType().matches("image\\/?\\w+")) {
			result.setSuccess(false);
			result.setMsg("Thông tin bài hát không hợp lệ");
		} else {
			Genre genre = genreDao.getById(song.getGenre().getId());
			if (genre == null) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			song.setGenre(genre);
			String fileurl = MainUtility.saveFile(file);
			song.setUrl(fileurl);
			String imageurl = MainUtility.saveSquareImage(image);
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
	public ResponseModel edit(Song song, MultipartFile image) {
		ResponseModel result = new ResponseModel();
		if (song.getGenre() == null || song.getGenre().getId() == 0 || song.getTitle() == null
				|| song.getTitle().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin bài hát không hợp lệ");
		} else if(songDao.checkOwner(song)) {
			Genre genre = genreDao.getById(song.getGenre().getId());
			if (genre == null) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			song.setGenre(genre);
			if(image != null && image.getContentType().matches("image\\/?\\w+")) {
				String imageurl = MainUtility.saveSquareImage(image);
				song.setImage(imageurl);
			}
			if (songDao.edit(song)) {
				result.setSuccess(true);
				result.setMsg("Sửa bài hát thành công");
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel getList(SongDTO searchDto) {
		ResponseModel result = new ResponseModel();
		List<Song> songs = songDao.getList(searchDto);
		songDao.getCountList(searchDto);
		if (songs == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(searchDto.getTotal());
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
		songDao.getCountList(searchDto);
		if (songs == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
			result.setTotal(0);
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(searchDto.getTotal());
			result.setContent(songs);
		}
		return result;
	}

	@Override
	public ResponseModel userGetById(String username, int id) {
		ResponseModel result = new ResponseModel();
		try {
			User user = userDao.getUserbyUsername(username);
			Song song = songDao.userGetById(user, id);
			if (song != null) {
				result.setSuccess(true);
				result.setMsg("Lấy thông tin bài hát thành công");
				result.setContent(song);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel getById(int id) {
		ResponseModel result = new ResponseModel();
		try {
			Song song = songDao.getById(id);
			if (song != null) {
				result.setSuccess(true);
				result.setMsg("Lấy thông tin bài hát thành công");
				result.setContent(song);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel userViewSong(View view) {
		ResponseModel result = new ResponseModel();
		View lastView = viewDao.getLastView(view.getUser().getUsername(), view.getSong().getId());
		Date listenTime = new Date();
		view.setListenTime(listenTime);
		if (lastView != null && view.getListenTime().getTime() - lastView.getListenTime().getTime() < 300000) {
			result.setSuccess(false);
			result.setMsg("Thêm lượt nghe thất bại");
		} else {
			Song song = songDao.getById(view.getSong().getId());
			User user = userDao.getUserbyUsername(view.getUser().getUsername());
			view.setUser(user);
			view.setSong(song);
			if (viewDao.addView(view)) {
				result.setSuccess(true);
				result.setMsg("Thêm lượt nghe thành công");
			}
		}
		return result;
	}

	public ResponseModel userFavoriteSong(Favorite favorite) {
		ResponseModel result = new ResponseModel();
		User user = userDao.getUserbyUsername(favorite.getUser().getUsername());
		favorite.setUser(user);
		Song song = songDao.getById(favorite.getSong().getId());
		favorite.setSong(song);
		if (song != null) {
			if (favoriteDao.checkFavorite(favorite)) {
				favoriteDao.removeFavorite(favorite);
				result.setSuccess(true);
				result.setMsg("Xóa bài hát ưa thích thành công");
			} else {
				favoriteDao.addFavorite(favorite);
				result.setSuccess(true);
				result.setMsg("Thêm bài hát ưa thích thành công");
			}
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel getRecommendations(int id) {
		ResponseModel result = new ResponseModel();
		List<Song> songs = songDao.getListRecommendation(id);
		if (songs == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(songs.size());
			result.setContent(songs);
		}
		return result;
	}

	@Transactional
	@Override
	public ResponseModel delete(Song song) {
		ResponseModel result = new ResponseModel();
		Favorite favorite = new Favorite();
		favorite.setSong(song);
		View view = new View();
		view.setSong(song);
		if(songDao.checkOwner(song)&&songDao.delete(song)&&favoriteDao.delete(favorite)&&viewDao.delete(view)) {
			result.setSuccess(true);
			result.setMsg("Xóa bài hát thành công");
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel userGetFollowingList(SongDTO searchDto) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userDao.getUserbyUsername(username);
		ResponseModel result = new ResponseModel();
		List<Song> songs = songDao.userGetFollowingList(user, searchDto);
		if (songs == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
			result.setTotal(0);
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(0);
			result.setContent(songs);
		}
		return result;
	}

	@Override
	public ResponseModel userGetFavoriteList(SongDTO searchDto) {
		SecurityContext context = SecurityContextHolder.getContext();
		String username = context.getAuthentication().getName();
		User user = userDao.getUserbyUsername(username);
		ResponseModel result = new ResponseModel();
		List<Song> songs = songDao.userGetFavoriteList(user, searchDto);
		if (songs == null) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra! Vui lòng thử lại");
		} else if (songs.isEmpty()) {
			result.setSuccess(true);
			result.setMsg("Không tìm được bài hát phù hợp");
			result.setTotal(0);
		} else {
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setTotal(0);
			result.setContent(songs);
		}
		return result;
	}

}
