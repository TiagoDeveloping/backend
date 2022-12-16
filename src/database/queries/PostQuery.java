package database.queries;

import java.sql.SQLException;

import com.alienarnchy.sql.ManagedQuery;
import com.alienarnchy.sql.SqlQuery;
import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

public class PostQuery implements ManagedQuery {
	
	public SqlQuery query;
	
	@SuppressWarnings("unused")
	private String postId;
	
	private static final String loginQuery = "SELECT `content` FROM `posts` WHERE `id`=\"{0}\"";
	
	private String result;
	
	public PostQuery(String postId, Database db) {
		this.postId = postId;
		String[] elements = {postId};
		
		query = new SqlQuery(loginQuery, elements, db);
		
		this.postId = postId;
	}
	
	/**
	 * @return json as string || 'null' if something went wrong(check log for error)
	 */
	public String getResult() {
		return this.result;
	}

	@Override
	public int execute() {
		try {
			query.execute();
			
			query.getResult().next();
			
			this.result = query.getResult().getString("content");
			
			return 1;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(Main.errorLog.getStream());
			return -1;
		} catch (InvalidDatabaseException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(Main.errorLog.getStream());
			return -1;
		}
	}

	@Override
	public String getQueryString() {
		return this.query.query;
	}
	
}
