package com.sinnguyen.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.sinnguyen.dao.PlaylistDao;
import com.sinnguyen.entities.Playlist;
import com.sinnguyen.entities.Song;
import com.sinnguyen.mapper.PlaylistMapper;
import com.sinnguyen.mapper.SongMapper;
import com.sinnguyen.model.PlaylistDTO;
import com.sinnguyen.util.MainUtility;

@Repository
public class PlaylistDaoImpl implements PlaylistDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean add(final Playlist playlist) {
		final String sql = "INSERT INTO playlist(user_id, title, image, create_time, type) VALUES (?, ?, ?, ?, ?)";
		try {
			playlist.setCreateTime(new Date());
			KeyHolder holder = new GeneratedKeyHolder();
			int row = this.jdbcTemplate.update((new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, playlist.getUser().getId());
					ps.setString(2, playlist.getTitle());
					ps.setString(3, playlist.getImage());
					ps.setString(4, MainUtility.dateToStringFormat(playlist.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					ps.setInt(5, 0);
					return ps;
				}
			}), holder);
			playlist.setId(holder.getKey().intValue());
			if (row > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Playlist> getList(final PlaylistDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT playlist.*, user.username, user.fullname FROM playlist INNER JOIN user ON playlist.user_id = user.id WHERE 1 = 1");
		if (searchDto.getUsername() != null) {
			sql.append(" AND user.username = ?");
		}
		if (searchDto.getUserId() != null) {
			sql.append(" AND user.id = ?");
		}
		if (searchDto.getKeyword() == null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(playlist.title) LIKE ?");
		if (searchDto.getSortField() != null) {
			if (searchDto.getSortField().equals("create_time")) {
				sql.append(" ORDER BY create_time");
			} else if (searchDto.getSortField().equals("title")) {
				sql.append(" ORDER BY title");
			} else {
				sql.append(" ORDER BY playlist.id");
			}
		} else {
			sql.append(" ORDER BY playlist.id");
		}
		if (searchDto.getSortOrder() != null && searchDto.getSortOrder().equals("descend")) {
			sql.append(" DESC");
		}
		if (searchDto.getResults() != null && searchDto.getPage() != null) {
			sql.append(" LIMIT ? OFFSET ?");
		}

		try {
			List<Playlist> playlists = this.jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					int count = 1;
					if (searchDto.getUsername() != null) {
						ps.setString(count++, searchDto.getUsername());
					}
					if (searchDto.getUserId() != null) {
						ps.setInt(count++, searchDto.getUserId());
					}
					ps.setString(count++, "%" + searchDto.getKeyword() + "%");
					if (searchDto.getResults() != null && searchDto.getPage() != null) {
						ps.setInt(count++, searchDto.getResults());
						ps.setInt(count++, (searchDto.getPage() - 1) * searchDto.getResults());
					}
				}
			}, new PlaylistMapper());
			return playlists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean checkSonginPLaylist(Song song, Playlist playlist) {
		try {
			String sql = "SELECT EXISTS (SELECT 1 FROM playlist_song WHERE playlist_id = ? AND song_id = ?)";
			if (this.jdbcTemplate.queryForObject(sql, Integer.class, playlist.getId(), song.getId()) == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addSong(Song song, Playlist playlist) {
		try {
			String sql = "INSERT INTO playlist_song(playlist_id, song_id) VALUES (?, ?)";
			Object[] newObj = new Object[] { playlist.getId(), song.getId() };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean check(Playlist playlist) {
		try {
			String sql = "SELECT EXISTS (SELECT 1 FROM playlist INNER JOIN user ON playlist.user_id = user.id "
					+ "WHERE playlist.id = ? AND user.username = ?)";
			if (this.jdbcTemplate.queryForObject(sql, Integer.class, playlist.getId(),
					playlist.getUser().getUsername()) == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeSong(Song song, Playlist playlist) {
		try {
			String sql = "DELETE FROM playlist_song WHERE playlist_id = ? AND song_id = ?";
			Object[] newObj = new Object[] { playlist.getId(), song.getId() };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Playlist getById(int id) {
		String sql = "SELECT playlist.*, user.username, user.fullname FROM playlist INNER JOIN user ON playlist.user_id = user.id "
				+ "WHERE playlist.id = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { id }, new PlaylistMapper());
			return (Playlist) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void getCountList(PlaylistDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(playlist.id) FROM playlist INNER JOIN user ON playlist.user_id = user.id WHERE 1 = 1");
		if (searchDto.getUsername() != null) {
			sql.append(" AND user.username = '" + searchDto.getUsername() + "'");
		}
		if (searchDto.getUserId() != null) {
			sql.append(" AND user.id = '" + searchDto.getUserId() + "'");
		}
		if (searchDto.getKeyword() == null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(playlist.title) LIKE '%" + searchDto.getKeyword() + "%'");
		try {
			int results = this.jdbcTemplate.queryForObject(sql.toString(), Integer.class);
			searchDto.setTotal(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean edit(Playlist playlist) {
		try {
			String sql = "";
			Object[] o = new Object[] {};
			if (playlist.getImage() == null) {
				sql = "UPDATE playlist SET playlist.title = ? WHERE playlist.id = ?";
				o = new Object[] { playlist.getTitle(), playlist.getId() };
			} else {
				sql = "UPDATE playlist SET playlist.title = ?, playlist.image = ? WHERE playlist.id = ?";
				o = new Object[] { playlist.getTitle(), playlist.getImage(), playlist.getId() };
			}
			if (this.jdbcTemplate.update(sql, o) > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Playlist playlist) {
		try {
			String sql = "DELETE FROM playlist WHERE playlist.id = ?";
			if (this.jdbcTemplate.update(sql, new Object[] { playlist.getId() }) > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean deleteAllSong(Playlist playlist) {
		try {
			String sql = "DELETE FROM playlist_song WHERE playlist_song.playlist_id = ?";
			if (this.jdbcTemplate.update(sql, new Object[] { playlist.getId() }) > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}
