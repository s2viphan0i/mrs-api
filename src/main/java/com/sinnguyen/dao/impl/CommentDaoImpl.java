package com.sinnguyen.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.sinnguyen.dao.CommentDao;
import com.sinnguyen.entities.Comment;
import com.sinnguyen.entities.Song;
import com.sinnguyen.model.CommentMapper;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.util.MainUtility;
import com.sinnguyen.util.PasswordGenerator;

@Repository
public class CommentDaoImpl implements CommentDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Comment> getBySongId(int songId, SearchDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT comment.*, user.fullname AS owner_fullname, user.username AS owner_username, user.avatar AS owner_avatar FROM comment "
				+ "INNER JOIN user ON comment.user_id = user.id WHERE comment.song_id = ? ORDER BY create_time DESC LIMIT ? OFFSET ?");
		if (searchDto.getResults() == null) {
			searchDto.setResults(10);
		}
		if (searchDto.getPage() == null) {
			searchDto.setPage(1);
		}
		// sql.append(" LIMIT "+searchDto.getResults()+" OFFSET "+(searchDto.getPage() -
		// 1) * searchDto.getResults());
		try {
			Object[] newObj = new Object[] { songId, searchDto.getResults(),
					(searchDto.getPage() - 1) * searchDto.getResults() };
			List<Comment> comments = this.jdbcTemplate.query(sql.toString(), newObj, new CommentMapper());
			return comments;
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public boolean add(final Song song, final Comment comment) {
		final String sql = "INSERT INTO comment(user_id, song_id, content, create_time) VALUES (?, ?, ?, ?)";
		try {
			comment.setCreateTime(new Date());
			KeyHolder holder = new GeneratedKeyHolder();
			int row = this.jdbcTemplate.update((new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, comment.getUser().getId());
					ps.setInt(2, song.getId());
					ps.setString(3, comment.getContent());
					ps.setString(4, MainUtility.dateToStringFormat(comment.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					return ps;
				}
			}), holder);
			comment.setId(holder.getKey().intValue());
			if (row > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void getCountList(int songId, SearchDTO searchDto) {
		String sql = "SELECT COUNT(comment.id) FROM comment WHERE comment.song_id = ?";
		try {
			int results = this.jdbcTemplate.queryForObject(sql, Integer.class, songId);
			searchDto.setTotal(results);
		} catch (Exception e) {
			e.printStackTrace();
			searchDto.setTotal(0);
		}
	}

	@Override
	public boolean delete(Comment comment) {
		String sql = "DELETE FROM comment WHERE comment.id = ? AND comment.user_id = ?";
		try {
			int row = this.jdbcTemplate.update(sql, comment.getId(), comment.getUser().getId());
			if(row>0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
