package com.sinnguyen.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sinnguyen.dao.SongDao;
import com.sinnguyen.entities.Song;
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
					MainUtility.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), song.getNote()};
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception ex) {

		}
		return false;
	}

}
