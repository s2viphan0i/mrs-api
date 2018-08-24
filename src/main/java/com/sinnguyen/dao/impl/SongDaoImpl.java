package com.sinnguyen.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.SongDao;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.SongDTO;
import com.sinnguyen.model.SongMapper;
import com.sinnguyen.util.MainUtility;

@Repository
public class SongDaoImpl implements SongDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean add(Song song) {
		try {
			String sql = "INSERT INTO song(genre_id, owner_id, title, url, lyric, mode, image, create_time, note) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			Object[] newObj = new Object[] { song.getGenre().getId(), song.getUser().getId(), song.getTitle(),
					song.getUrl(), song.getLyric(), song.isMode(), song.getImage(),
					MainUtility.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), song.getNote() };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception ex) {

		}
		return false;
	}

	@Override
	public List<Song> getList(SongDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT song.id, song.title, song.image, song.url,"
				+ "(SELECT COUNT(user_id) FROM view v WHERE v.song_id = song.id");
		if (searchDto.getViewStartDate() != null && searchDto.getViewEndDate() != null) {
			sql.append(" AND v.timestamp<'"
					+ MainUtility.dateToStringFormat(searchDto.getViewEndDate(), "yyyy-MM-dd HH:mm:ss") + "' "
					+ "AND v.timestamp>'"
					+ MainUtility.dateToStringFormat(searchDto.getViewStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		sql.append(") AS views, (SELECT COUNT(user_id) FROM favorite f WHERE f.song_id = song.id");
		if (searchDto.getFavoriteStartDate() != null && searchDto.getFavoriteEndDate() != null) {
			sql.append(" AND f.timestamp<'"
					+ MainUtility.dateToStringFormat(searchDto.getFavoriteEndDate(), "yyyy-MM-dd HH:mm:ss") + "' "
					+ "AND f.timestamp>'"
					+ MainUtility.dateToStringFormat(searchDto.getFavoriteStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		sql.append(
				") AS favorites, false AS favorited, user.username AS owner_username, user.fullname AS owner_fullname, owner_id FROM song "
						+ "INNER JOIN user WHERE song.owner_id = user.id");
		if (searchDto.getKeyword() != null) {
			sql.append(" AND LOWER(song.title) LIKE '%" + searchDto.getKeyword().toLowerCase() + "%'");
		}
		if (searchDto.getStartDate() != null) {
			sql.append(" AND song.create_time > '"
					+ MainUtility.dateToStringFormat(searchDto.getStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (searchDto.getEndDate() != null) {
			sql.append(" AND song.create_time < '"
					+ MainUtility.dateToStringFormat(searchDto.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (searchDto.getGenreId() != null) {
			sql.append(" AND song.genre_id = " + searchDto.getGenreId());
		}
		if (searchDto.getUserId() != null) {
			sql.append(" AND song.owner_id = " + searchDto.getUserId());
		}
		if (searchDto.getUsername() != null) {
			sql.append(" AND user.username LIKE '%" + searchDto.getUsername() + "%'");
		}
		if (searchDto.getSortField() != null) {
			if (searchDto.getSortField().equals("views")) {
				sql.append(" ORDER BY views");
			} else if (searchDto.getSortField().equals("favorites")) {
				sql.append(" ORDER BY favorites");
			} else {
				sql.append(" ORDER BY song.id");
			}
		} else {
			sql.append(" ORDER BY song.id");
		}
		if (searchDto.getSortOrder() != null && searchDto.getSortOrder().equals("descend")) {
			sql.append(" DESC");
		}
		if (searchDto.getResults() == null) {
			searchDto.setResults(10);
		}
		if (searchDto.getPage() == null) {
			searchDto.setPage(1);
		}
		sql.append(
				" LIMIT " + searchDto.getResults() + " OFFSET " + (searchDto.getPage() - 1) * searchDto.getResults());
		try {
			List<Song> songs = new ArrayList<Song>();
			List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql.toString());
			for (Map row : rows) {
				Song song = new Song();
				song.setId((Integer) (row.get("id")));
				song.setTitle((String) (row.get("title")));
				song.setImage((String) (row.get("image")));
				song.setUrl((String) (row.get("url")));
				song.setViews(Integer.parseInt(row.get("views").toString()));
				song.setFavorites(Integer.parseInt(row.get("favorites").toString()));
				User user = new User();
				user.setId((Integer) row.get("owner_id"));
				user.setUsername((String) row.get("owner_username"));
				user.setFullname((String) (row.get("owner_fullname")));
				song.setUser(user);
				songs.add(song);
			}
			return songs;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void getCountList(SongDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(song.id) FROM song WHERE 1=1");

		if (searchDto.getKeyword() != null) {
			sql.append(" AND LOWER(song.title) LIKE '%" + searchDto.getKeyword().toLowerCase() + "%'");
		}
		if (searchDto.getStartDate() != null) {
			sql.append(" AND song.create_time > '"
					+ MainUtility.dateToStringFormat(searchDto.getStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (searchDto.getEndDate() != null) {
			sql.append(" AND song.create_time < '"
					+ MainUtility.dateToStringFormat(searchDto.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (searchDto.getGenreId() != null) {
			sql.append(" AND song.genre_id = " + searchDto.getGenreId());
		}
		if (searchDto.getUserId() != null) {
			sql.append(" AND song.owner_id = " + searchDto.getUserId());
		}
		if (searchDto.getUsername() != null) {
			sql.append(" AND user.username LIKE '%" + searchDto.getUsername() + "%'");
		}
		try {
			int results = this.jdbcTemplate.queryForObject(sql.toString(), Integer.class);
			searchDto.setTotal(results);
		} catch (Exception e) {
			e.printStackTrace();
			searchDto.setTotal(0);
		}
	}

	@Override
	public List<Song> userGetList(User user, SongDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT song.id, song.title, song.image, song.url,"
				+ "(SELECT COUNT(user_id) FROM view v WHERE v.song_id = song.id");
		if (searchDto.getViewStartDate() != null && searchDto.getViewEndDate() != null) {
			sql.append(" AND v.timestamp<'"
					+ MainUtility.dateToStringFormat(searchDto.getViewEndDate(), "yyyy-MM-dd HH:mm:ss") + "' "
					+ "AND v.timestamp>'"
					+ MainUtility.dateToStringFormat(searchDto.getViewStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		sql.append(") AS views, (SELECT COUNT(user_id) FROM favorite f WHERE f.song_id = song.id");
		if (searchDto.getFavoriteStartDate() != null && searchDto.getFavoriteEndDate() != null) {
			sql.append(" AND f.timestamp<'"
					+ MainUtility.dateToStringFormat(searchDto.getFavoriteEndDate(), "yyyy-MM-dd HH:mm:ss") + "' "
					+ "AND f.timestamp>'"
					+ MainUtility.dateToStringFormat(searchDto.getFavoriteStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		sql.append(") AS favorites,(SELECT EXISTS (SELECT 1 FROM favorite f WHERE f.song_id = song.id AND f.user_id = "
				+ user.getId() + ")) "
				+ "AS favorited, user.username AS owner_username, user.fullname AS owner_fullname, owner_id FROM song "
				+ "INNER JOIN user WHERE song.owner_id = user.id");
		if (searchDto.getKeyword() != null) {
			sql.append(" AND LOWER(song.title) LIKE '%" + searchDto.getKeyword().toLowerCase() + "%'");
		}
		if (searchDto.getStartDate() != null) {
			sql.append(" AND song.create_time > '"
					+ MainUtility.dateToStringFormat(searchDto.getStartDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (searchDto.getEndDate() != null) {
			sql.append(" AND song.create_time < '"
					+ MainUtility.dateToStringFormat(searchDto.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'");
		}
		if (searchDto.getGenreId() != null) {
			sql.append(" AND song.genre_id = " + searchDto.getGenreId());
		}
		if (searchDto.getUserId() != null) {
			sql.append(" AND song.owner_id = " + searchDto.getUserId());
		}
		if (searchDto.getUsername() != null) {
			sql.append(" AND user.username LIKE '%" + searchDto.getUsername() + "%'");
		}
		if (searchDto.getSortField() != null) {
			if (searchDto.getSortField().equals("views")) {
				sql.append(" ORDER BY views");
			} else if (searchDto.getSortField().equals("favorites")) {
				sql.append(" ORDER BY favorites");
			} else {
				sql.append(" ORDER BY song.id");
			}
		} else {
			sql.append(" ORDER BY song.id");
		}
		if (searchDto.getSortOrder() != null && searchDto.getSortOrder().equals("descend")) {
			sql.append(" DESC");
		}
		if (searchDto.getResults() == null) {
			searchDto.setResults(10);
		}
		if (searchDto.getPage() == null) {
			searchDto.setPage(1);
		}
		sql.append(
				" LIMIT " + searchDto.getResults() + " OFFSET " + (searchDto.getPage() - 1) * searchDto.getResults());
		try {
			List<Song> songs = new ArrayList<Song>();
			List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql.toString());
			for (Map row : rows) {
				Song song = new Song();
				song.setId((Integer) (row.get("id")));
				song.setTitle((String) (row.get("title")));
				song.setImage((String) (row.get("image")));
				song.setUrl((String) (row.get("url")));
				song.setViews(Integer.parseInt(row.get("views").toString()));
				song.setFavorites(Integer.parseInt(row.get("favorites").toString()));
				song.setFavorited(row.get("favorited").toString().equals("0") ? false : true);
				User u = new User();
				u.setId((Integer) row.get("owner_id"));
				u.setUsername((String) row.get("owner_username"));
				u.setFullname((String) (row.get("owner_fullname")));
				song.setUser(u);
				songs.add(song);
			}
			return songs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Song getById(int id) {
		String sql = "SELECT song.*, genre.name AS genre_name, (SELECT COUNT(user_id) FROM view v WHERE song.id = v.song_id) AS views, "
				+ "(SELECT COUNT(user_id) FROM favorite f WHERE f.song_id = song.id) AS favorites, false AS favorited "
				+ "FROM song INNER JOIN genre ON song.genre_id = genre.id WHERE song.id = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { id }, new SongMapper());
			return (Song) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}

	public Song userGetById(User user, int id) {
		String sql = "SELECT song.*, genre.name AS genre_name, (SELECT COUNT(user_id) FROM view v WHERE song.id = v.song_id) AS views, "
				+ "(SELECT COUNT(user_id) FROM favorite f WHERE f.song_id = song.id) AS favorites, "
				+ "(SELECT EXISTS (SELECT 1 FROM favorite f WHERE f.song_id = song.id AND f.user_id = ?)) AS favorited "
				+ "FROM song INNER JOIN genre ON song.genre_id = genre.id WHERE song.id = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { user.getId(), id },
					new SongMapper());
			return (Song) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Song> getSongbyPlaylistId(int playlistId) {
		String sql = "SELECT song_id FROM playlist_song WHERE playlist_id = ?";
		try {
			List<Song> songs = new ArrayList<Song>();
			List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql.toString(), playlistId);
			for (Map row : rows) {
				Song song = new Song();
				song.setId((Integer) (row.get("song_id")));
				songs.add(song);
			}
			return songs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean check(Song song) {
		try {
			String sql = "SELECT EXISTS (SELECT 1 FROM song WHERE song.id = ?)";
			if (this.jdbcTemplate.queryForObject(sql, Integer.class, song.getId()) == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Song> getSongDetailbyPlaylistId(int playlistId) {
		String sql = "SELECT song.*, user.fullname FROM song INNER JOIN playlist_song ON playlist_song.song_id = song.id "
				+ "INNER JOIN user ON song.owner_id = user.id WHERE playlist_song.playlist_id = ?";
		try {
			List<Song> songs = new ArrayList<Song>();
			List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql, playlistId);
			for (Map row : rows) {
				Song song = new Song();
				song.setId((Integer) (row.get("id")));
				song.setTitle((String)row.get("title"));
				song.setUrl((String)row.get("url"));
				song.setImage((String)row.get("image"));
				User user = new User();
				user.setId((Integer) (row.get("owner_id")));
				user.setFullname((String)row.get("fullname"));
				song.setUser(user);
				songs.add(song);
			}
			return songs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
