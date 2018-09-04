package com.sinnguyen.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.sinnguyen.dao.UserDao;
import com.sinnguyen.entities.Song;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.SearchDTO;
import com.sinnguyen.model.UserDTO;
import com.sinnguyen.model.UserMapper;
import com.sinnguyen.util.MainUtility;
import com.sinnguyen.util.PasswordGenerator;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean checkUsername(User user) {
		String sql = "SELECT EXISTS (SELECT 1 FROM user WHERE username = '" + user.getUsername() + "' OR email = '"
				+ user.getEmail() + "')";
		if (this.jdbcTemplate.queryForObject(sql, Integer.class) == 1) {
			return false;
		}
		return true;
	}

	public boolean add(final User user) {
		try {
			final String sql = "INSERT INTO user (username, password, fullname, avatar, birthdate, email, phone, activated, role, note) VALUES (?,?,?,?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			int row = this.jdbcTemplate.update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, user.getUsername());
					ps.setString(2, PasswordGenerator.genPassword(user.getPassword()));
					ps.setString(3, user.getFullname());
					ps.setString(4, "default.png");
					if(user.getBirthdate()!=null) {
						ps.setString(5, MainUtility.dateToStringFormat(user.getBirthdate(), "yyyy-MM-dd HH:mm:ss"));
					}else {
						ps.setNull(5, Types.DATE);
					}
					ps.setString(6, user.getEmail());
					if(user.getPhone()!=null) {
						ps.setString(7, user.getPhone());
					} else {
						ps.setNull(7, Types.VARCHAR);
					}
					ps.setBoolean(8, false);
					ps.setString(9, "ROLE_USER");
					ps.setString(10, user.getNote());

					return ps;
				}
			}, holder);
			if (row > 0) {
				user.setId(holder.getKey().intValue());
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean insertActivation(User user) {
		try {
			String sql = "UPDATE user SET code = ? WHERE id = ? AND activated = ?";
			String code = user.getId() + System.currentTimeMillis() + "";
			Object[] newObj = new Object[] { code, user.getId(), false };
			int row = this.jdbcTemplate.update(sql, newObj);
			user.setCode(code);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean activate(String code) {
		try {
			String sql = "UPDATE user SET activated = ? WHERE code = ?";
			Object[] newObj = new Object[] { true, code };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public boolean editByUsername(User user) {
		try {
			String sql = "UPDATE user SET fullname = ?, birthdate = ?, phone = ?, note = ?, avatar = ? WHERE username = ?";
			Object[] newObj = new Object[] { user.getFullname(), (user.getBirthdate()==null)?null:MainUtility.dateToStringFormat(user.getBirthdate(), "yyyy-MM-dd HH:mm:ss"),
					user.getPhone(), user.getNote(), user.getAvatar(), user.getUsername() };
			if(user.getAvatar()==null) {
				sql = "UPDATE user SET fullname = ?, birthdate = ?, phone = ?, note = ? WHERE username = ?";
				newObj = new Object[] { user.getFullname(), (user.getBirthdate()==null)?null:MainUtility.dateToStringFormat(user.getBirthdate(), "yyyy-MM-dd HH:mm:ss"),
						user.getPhone(), user.getNote(), user.getUsername() };
			}
			
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean delete(User user) {
		// TODO
		return false;
	}

	public boolean getById(int id) {
		// TODO
		return false;
	}

	public boolean search(SearchDTO searchDTO) {
		// TODO
		return false;
	}

	public User getUserbyEmail(String email) {
		String sql = "SELECT * FROM user WHERE email = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { email }, new UserMapper());
			return (User) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}

	public List<User> getAllUser(){
		String sql = "SELECT * FROM user WHERE activated = ?";
		try {
			List<User> users = this.jdbcTemplate.query(sql, new Object[] { true }, new UserMapper());
			return users;
		} catch (Exception e) {
			return null;
		}
	}
	
	public User getUserbyUsername(String username) {
		String sql = "SELECT user.*, (SELECT COUNT(id) FROM follow f WHERE user.id = f.following_id) AS followers, "
				+ "(SELECT COUNT(id) FROM follow f WHERE user.id = f.follower_id) AS followings, false AS followed "
				+ "FROM user WHERE username = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { username }, new UserMapper());
			return (User) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}
	
	public User userGetUserbyUsername(String username, int currentId) {
		String sql = "SELECT user.*, (SELECT COUNT(id) FROM follow f WHERE user.id = f.following_id) AS followers, "
				+ "(SELECT COUNT(id) FROM follow f WHERE user.id = f.follower_id) AS followings, "
				+ "(SELECT EXISTS (SELECT 1 FROM follow f WHERE f.following_id = user.id AND f.follower_id=?)) AS followed "
				+ "FROM user WHERE username = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] {currentId, username }, new UserMapper());
			return (User) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean changePassword(User user) {
		try {
			String sql = "UPDATE user SET password = ? WHERE id = ?";
			Object[] newObj = new Object[] { PasswordGenerator.genPassword(user.getPassword()), user.getId() };
			int row = this.jdbcTemplate.update(sql, newObj);
			if (row > 0) {
				return true;
			}
		} catch (Exception ex) {
			
		}
		return false;
	}

	@Override
	public User getUserbyId(int id) {
		String sql = "SELECT user.*, (SELECT COUNT(id) FROM follow f WHERE user.id = f.following_id) AS followers,"
				+ "(SELECT COUNT(id) FROM follow f WHERE user.id = f.follower_id) AS followings, false AS followed "
				+ "FROM user WHERE id = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] { id }, new UserMapper());
			return (User) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public User userGetUserbyId(int id, int currentId) {
		String sql = "SELECT user.*, (SELECT COUNT(id) FROM follow f WHERE user.id = f.following_id) AS followers,"
				+ "(SELECT COUNT(id) FROM follow f WHERE user.id = f.follower_id) AS followings,"
				+ "(SELECT EXISTS (SELECT 1 FROM follow f WHERE f.following_id = user.id AND f.follower_id=?)) AS followed "
				+ "FROM user WHERE id = ?";
		try {
			Object queryForObject = this.jdbcTemplate.queryForObject(sql, new Object[] {currentId, id }, new UserMapper());
			return (User) queryForObject;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<User> userGetListFollowing(User user, UserDTO searchDto) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT * FROM (SELECT user.*, (SELECT COUNT(following_id) FROM follow f WHERE f.follower_id = user.id) AS followings, ");

		sql.append("(SELECT COUNT(follower_id) FROM follow f WHERE f.following_id = user.id");
		if(searchDto.getFollowStartDate()!=null&&searchDto.getFollowEndDate()!=null) {
			sql.append(" AND f.timestamp<'"+MainUtility.dateToStringFormat(searchDto.getFollowEndDate(), "yyyy-MM-dd HH:mm:ss")+"' "
					+ "AND f.timestamp>'"+MainUtility.dateToStringFormat(searchDto.getFollowStartDate(), "yyyy-MM-dd HH:mm:ss")+"'");
		}
		sql.append(") AS followers, (SELECT exists(SELECT 1 FROM follow f WHERE f.following_id = user.id AND f.follower_id = ?)) AS followed "
				+ "FROM user WHERE 1=1");
		if(searchDto.getKeyword()==null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(user.fullname) LIKE ?) AS T WHERE T.followed = 1");
		if(searchDto.getSortField()!=null) {
			if(searchDto.getSortField().equals("followers")) {
				sql.append(" ORDER BY followers");
			} else {
				sql.append(" ORDER BY id");
			}
		} else {
			sql.append(" ORDER BY id");
		}
		if(searchDto.getSortOrder()!=null&&searchDto.getSortOrder().equals("descend")) {
			sql.append(" DESC");
		}
		if(searchDto.getResults()==null) {
			searchDto.setResults(10);
		}
		if(searchDto.getPage()==null) {
			searchDto.setPage(1);
		}
		sql.append(" LIMIT "+searchDto.getResults()+" OFFSET "+(searchDto.getPage() - 1) * searchDto.getResults());
		try {
			List<User> users = this.jdbcTemplate.query(sql.toString(), new UserMapper(), user.getId(), "%"+searchDto.getKeyword().toLowerCase()+"%");
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void getCountListFollowing(User user, UserDTO searchDto) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT COUNT(id) FROM (SELECT user.*, (SELECT COUNT(following_id) FROM follow f WHERE f.follower_id = user.id) AS followings, ");

		sql.append("(SELECT COUNT(follower_id) FROM follow f WHERE f.following_id = user.id");
		if(searchDto.getFollowStartDate()!=null&&searchDto.getFollowEndDate()!=null) {
			sql.append(" AND f.timestamp<'"+MainUtility.dateToStringFormat(searchDto.getFollowEndDate(), "yyyy-MM-dd HH:mm:ss")+"' "
					+ "AND f.timestamp>'"+MainUtility.dateToStringFormat(searchDto.getFollowStartDate(), "yyyy-MM-dd HH:mm:ss")+"'");
		}
		sql.append(") AS followers, (SELECT exists(SELECT 1 FROM follow f WHERE f.following_id = user.id AND f.follower_id = ?)) AS followed "
				+ "FROM user WHERE 1=1");
		if(searchDto.getKeyword()==null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(user.fullname) LIKE ?) AS T WHERE T.followed = 1");
		try {
			int results = this.jdbcTemplate.queryForObject(sql.toString(), Integer.class, user.getId(), "%"+searchDto.getKeyword().toLowerCase()+"%");
			searchDto.setTotal(results);
		} catch (Exception e) {
			e.printStackTrace();
			searchDto.setTotal(0);
		}
	}
	
	@Override
	public List<User> userGetList(User user, UserDTO searchDto) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT user.*, (SELECT COUNT(following_id) FROM follow f WHERE f.follower_id = user.id) AS followings, ");

		sql.append("(SELECT COUNT(follower_id) FROM follow f WHERE f.following_id = user.id");
		if(searchDto.getFollowStartDate()!=null&&searchDto.getFollowEndDate()!=null) {
			sql.append(" AND f.timestamp<'"+MainUtility.dateToStringFormat(searchDto.getFollowEndDate(), "yyyy-MM-dd HH:mm:ss")+"' "
					+ "AND f.timestamp>'"+MainUtility.dateToStringFormat(searchDto.getFollowStartDate(), "yyyy-MM-dd HH:mm:ss")+"'");
		}
		sql.append(") AS followers, (SELECT exists(SELECT 1 FROM follow f WHERE f.following_id = user.id AND f.follower_id = ?)) AS followed "
				+ "FROM user WHERE 1=1");
		if(searchDto.getKeyword()==null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(user.fullname) LIKE ?");
		if(searchDto.getSortField()!=null) {
			if(searchDto.getSortField().equals("followers")) {
				sql.append(" ORDER BY followers");
			} else {
				sql.append(" ORDER BY user.id");
			}
		} else {
			sql.append(" ORDER BY user.id");
		}
		if(searchDto.getSortOrder()!=null&&searchDto.getSortOrder().equals("descend")) {
			sql.append(" DESC");
		}
		if(searchDto.getResults()==null) {
			searchDto.setResults(10);
		}
		if(searchDto.getPage()==null) {
			searchDto.setPage(1);
		}
		sql.append(" LIMIT "+searchDto.getResults()+" OFFSET "+(searchDto.getPage() - 1) * searchDto.getResults());
		try {
			List<User> users = this.jdbcTemplate.query(sql.toString(), new UserMapper(),user.getId(), "%"+searchDto.getKeyword().toLowerCase()+"%");
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> getList(UserDTO searchDto) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT user.*, (SELECT COUNT(following_id) FROM follow f WHERE f.follower_id = user.id) AS followings, ");

		sql.append("(SELECT COUNT(follower_id) FROM follow f WHERE f.following_id = user.id");
		if(searchDto.getFollowStartDate()!=null&&searchDto.getFollowEndDate()!=null) {
			sql.append(" AND f.timestamp<'"+MainUtility.dateToStringFormat(searchDto.getFollowEndDate(), "yyyy-MM-dd HH:mm:ss")+"' "
					+ "AND f.timestamp>'"+MainUtility.dateToStringFormat(searchDto.getFollowStartDate(), "yyyy-MM-dd HH:mm:ss")+"'");
		}
		sql.append(") AS followers, 0 AS followed FROM user WHERE 1=1");
		if(searchDto.getKeyword()==null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(user.fullname) LIKE ?");
		if(searchDto.getSortField()!=null) {
			if(searchDto.getSortField().equals("followers")) {
				sql.append(" ORDER BY followers");
			}
		} else {
			sql.append(" ORDER BY song.id");
		}
		if(searchDto.getSortOrder()!=null&&searchDto.getSortOrder().equals("descend")) {
			sql.append(" DESC");
		}
		if(searchDto.getResults()==null) {
			searchDto.setResults(10);
		}
		if(searchDto.getPage()==null) {
			searchDto.setPage(1);
		}
		sql.append(" LIMIT "+searchDto.getResults()+" OFFSET "+(searchDto.getPage() - 1) * searchDto.getResults());
		try {
			List<User> users = this.jdbcTemplate.query(sql.toString(), new UserMapper(), "%"+searchDto.getKeyword().toLowerCase()+"%");
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void getCountList(UserDTO searchDto) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(user.id) FROM user WHERE 1=1");

		if(searchDto.getKeyword()==null) {
			searchDto.setKeyword("");
		}
		sql.append(" AND LOWER(user.fullname) LIKE ?");

		try {
			int results = this.jdbcTemplate.queryForObject(sql.toString(), Integer.class, "%"+searchDto.getKeyword().toLowerCase()+"%");
			searchDto.setTotal(results);
		} catch (Exception e) {
			e.printStackTrace();
			searchDto.setTotal(0);
		}
	}

}
