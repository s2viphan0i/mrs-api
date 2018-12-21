package com.sinnguyen.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinnguyen.dao.CommentDao;
import com.sinnguyen.dao.FavoriteDao;
import com.sinnguyen.dao.FollowDao;
import com.sinnguyen.dao.GenreDao;
import com.sinnguyen.dao.SongDao;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.dao.ViewDao;
import com.sinnguyen.entities.Favorite;
import com.sinnguyen.entities.Follow;
import com.sinnguyen.entities.Genre;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.entities.View;
import com.sinnguyen.model.ResponseModel;
import com.sinnguyen.service.AdminService;
import com.sinnguyen.util.MainUtility;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	GenreDao genreDao;

	@Autowired
	SongDao songDao;

	@Autowired
	UserDao userDao;

	@Autowired
	FavoriteDao favoriteDao;

	@Autowired
	ViewDao viewDao;

	@Autowired
	FollowDao followDao;

	@Autowired
	CommentDao commentDao;

	@Override
	public ResponseModel editSong(Song song, MultipartFile image) {
		ResponseModel result = new ResponseModel();
		if (song.getGenre() == null || song.getGenre().getId() == 0 || song.getTitle() == null
				|| song.getTitle().equals("")) {
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
			if (image != null && image.getContentType().matches("image\\/?\\w+")) {
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
		}
		return result;
	}

	@Override
	public ResponseModel editUser(User user, MultipartFile file) {
		ResponseModel result = new ResponseModel();
		if (user.getUsername() == null || user.getUsername().equals("") || user.getFullname() == null
				|| user.getFullname().equals("")) {
			result.setSuccess(false);
			result.setMsg("Thông tin người dùng không hợp lệ");
		} else {
			if (file != null) {
				String filename = MainUtility.saveSquareImage(file);
				user.setAvatar(filename);

			}
			if (userDao.edit(user)) {
				result.setSuccess(true);
				result.setMsg("Sửa thông tin người dùng thành công");
				result.setContent(user);
			} else {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra! Vui lòng kiểm tra lại");
			}
		}
		return result;
	}

	@Override
	public ResponseModel deleteSong(Song song) {
		ResponseModel result = new ResponseModel();
		Favorite favorite = new Favorite();
		favorite.setSong(song);
		View view = new View();
		view.setSong(song);
		if (songDao.delete(song) && favoriteDao.deleteAllBySong(song.getId()) && viewDao.deleteAllBySong(song.getId())
				&& commentDao.deleteAllBySong(song.getId())) {
			result.setSuccess(true);
			result.setMsg("Xóa bài hát thành công");
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel deleteUser(User user) {
		ResponseModel result = new ResponseModel();
		if (userDao.deactivated(user)) {
			result.setSuccess(true);
			result.setMsg("Khóa tài khoản thành công");
		} else {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
		}
		return result;
	}

	@Override
	public ResponseModel reportUpload(String from, String to) {
		ResponseModel result = new ResponseModel();
		try {
			Date fromDate = MainUtility.stringtoDate(from, "dd-MM-yyyy");
			Date toDate = MainUtility.stringtoDate(to, "dd-MM-yyyy");
			if (fromDate.compareTo(toDate) == 1) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			List<Map<String, Object>> report = songDao.reportUpload(fromDate, toDate);
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(report);
			return result;
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			return result;
		}
	}

	@Override
	public ResponseModel reportView(String from, String to) {
		ResponseModel result = new ResponseModel();
		try {
			Date fromDate = MainUtility.stringtoDate(from, "dd-MM-yyyy");
			Date toDate = MainUtility.stringtoDate(to, "dd-MM-yyyy");
			if (fromDate.compareTo(toDate) == 1) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			int views = viewDao.reportView(fromDate, toDate);
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(views);
			return result;
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			return result;
		}
	}

	@Override
	public ResponseModel reportFavorite(String from, String to) {
		ResponseModel result = new ResponseModel();
		try {
			Date fromDate = MainUtility.stringtoDate(from, "dd-MM-yyyy");
			Date toDate = MainUtility.stringtoDate(to, "dd-MM-yyyy");
			if (fromDate.compareTo(toDate) == 1) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			int views = favoriteDao.reportFavorite(fromDate, toDate);
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(views);
			return result;
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			return result;
		}
	}

	@Override
	public ResponseModel reportFollow(String from, String to) {
		ResponseModel result = new ResponseModel();
		try {
			Date fromDate = MainUtility.stringtoDate(from, "dd-MM-yyyy");
			Date toDate = MainUtility.stringtoDate(to, "dd-MM-yyyy");
			if (fromDate.compareTo(toDate) == 1) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			int follows = followDao.reportFollow(fromDate, toDate);
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(follows);
			return result;
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			return result;
		}
	}

	@Override
	public ResponseModel reportUser(String from, String to) {
		ResponseModel result = new ResponseModel();
		try {
			Date fromDate = MainUtility.stringtoDate(from, "dd-MM-yyyy");
			Date toDate = MainUtility.stringtoDate(to, "dd-MM-yyyy");
			if (fromDate.compareTo(toDate) == 1) {
				result.setSuccess(false);
				result.setMsg("Có lỗi xảy ra vui lòng thử lại");
				return result;
			}
			int users = userDao.reportUser(fromDate, toDate);
			result.setSuccess(true);
			result.setMsg("Lấy dữ liệu thành công");
			result.setContent(users);
			return result;
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("Có lỗi xảy ra vui lòng thử lại");
			return result;
		}
	}

}
