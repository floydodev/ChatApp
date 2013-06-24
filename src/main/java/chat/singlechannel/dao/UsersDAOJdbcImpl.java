package chat.singlechannel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import chat.singlechannel.dto.User;

public class UsersDAOJdbcImpl extends JdbcDaoSupport implements UsersDAO {

	private final static Log log = LogFactory.getLog(UsersDAOJdbcImpl.class);
	private Map<String, User> cache = new HashMap<String, User>();
	//private SimpleJdbcTemplate jdbcTemplate; 
	
	public UsersDAOJdbcImpl() {
		//this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.UsersDAO#containsKey(java.lang.String)
	 */
	public boolean containsKey(String userEmailAddress) {
		// TODO Auto-generated method stub
		return get(userEmailAddress) != null;
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.UsersDAO#get(java.lang.String)
	 */
	public User get(String userEmailAddress) {
		if (!cache.containsKey(userEmailAddress)) {
			String sql = "select * from Users where email_address = ?";
			List<User> users = getJdbcTemplate().query(sql,
					new RowMapper<User>() {
				
						public User mapRow(ResultSet rs, int rowNum) throws SQLException {
							String emailAddress = rs.getString("email_address");
							String displayName = rs.getString("display_name");
							return new User(emailAddress, displayName);
						}
					}, userEmailAddress);
		
			if (users.isEmpty()) {
				return null;
			} else {
				cache.put(userEmailAddress, users.get(0));
				return users.get(0);
			}
		} else {
			return cache.get(userEmailAddress);
		}
	}

	/* (non-Javadoc)
	 * @see chat.singlechannel.dao.UsersDAO#put(java.lang.String, chat.singlechannel.dto.User)
	 */
	public void put(String emailAddress, User user) {
		// Don't attempt to insert into DB if it's already there
		if (get(emailAddress) != null) {
			return;
		}
		String sql = "insert into Users (email_address, display_name) values (?, ?)";
		getJdbcTemplate().update(sql, emailAddress, user.getDisplayName());
		log.info("Inserted 1 record into Users table");
	}

	public int getLastMessageId(String userEmailAddress) {
		return cache.get(userEmailAddress).getLastMessageId();
	}

	public void setLastMessageId(String userEmailAddress, int lastMessageId) {
		// Workaround for not having this field in the DB schema yet
		cache.get(userEmailAddress).setLastMessageId(lastMessageId);
	}

}
