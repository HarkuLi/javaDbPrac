package dao.user;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import rowMapper.user.UserIntMapper;

public class UserIntDao {
	private JdbcTemplate jdbcObj;
	
	private final String tableName = "userInterest";

	public UserIntDao(DataSource dataSource) {
		jdbcObj = new JdbcTemplate(dataSource);
	}
	
	public void create(String userId, String interest) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (userId, interest)";
		sqlStr 		 += " values (?, ?)";
		
		jdbcObj.update(sqlStr, new Object[] {userId, interest});
	}
	
	/**
	 * 
	 * @param userId {String} user's id
	 * @return {ArrayList<String>} return a list of interest id of the user
	 */
	public ArrayList<String> read(String userId) {
		String sqlStr = "select interest" +
				        " from " + tableName +
			            " where userId = ?";
		
		ArrayList<String> rstList = new ArrayList<String>(jdbcObj.query(sqlStr, new Object[] {userId}, new UserIntMapper()));
		
		return rstList;
	}

	public void delete(String userId) {
		String sqlStr = "delete from " + tableName +
						" where userId = ?";
		
		jdbcObj.update(sqlStr, new Object[] {userId});
	}
}
