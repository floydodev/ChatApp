package chat.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class MessageIdFountainAtomicIntJdbcImpl extends JdbcDaoSupport implements MessageIdFountain {

	private AtomicInteger messageIdFountain;
	
	public MessageIdFountainAtomicIntJdbcImpl() {
		

	}

	private boolean initialised = false;
	
	public int getNextId() {
		if (!initialised) {
			initialise();
		}
		return messageIdFountain.incrementAndGet();
	}
	
	private void initialise() {
		List<Integer> latestMessageId = getJdbcTemplate().query("select max(id) from Messages", new RowMapper<Integer>(){

			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
			
		});
		messageIdFountain = new AtomicInteger();
		messageIdFountain.set(latestMessageId.get(0));
	}

	public int getLastId() {
		return messageIdFountain.get();
	}
	
}
