package database.queries;

import java.sql.SQLException;

import com.alienarnchy.sql.ManagedQuery;
import com.alienarnchy.sql.SqlQuery;
import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

/**
 * 
 * @author Tiago Ramos
 *
 */
public class LoginQuery implements ManagedQuery {

	public SqlQuery query;
	
	@SuppressWarnings("unused")
	private String username, password;
	
	private static final String loginQuery = "SELECT `password` FROM `users` WHERE `username`=\"{0}\"";
	
	private boolean result;
	
	public LoginQuery(String username, String password, Database db) {
		String[] elements = {username};
		query = new SqlQuery(loginQuery, elements, db);
		
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Connects to database and executes query
	 * 
	 * @return wether login is valid
	 */
	public boolean isLoginValid() {
		try {
			query.execute();
			
			query.getResult().next();
			
			String resultingPassword = query.getResult().getString("password");
			query.close();
			
			this.result = resultingPassword.equals(this.password);
		} catch (SQLException | InvalidDatabaseException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
		}
		
		return false;
	}

	@Override
	public int execute() {
		try {
			query.execute();
			
			query.getResult().next();
			
			String resultingPassword = query.getResult().getString("password");
			query.close();
			
			this.result = resultingPassword.equals(this.password);
			
			return 1;
		} catch (SQLException | InvalidDatabaseException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
			return -1;
		} catch (NullPointerException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
			return -1;
		}
	}

	@Override
	public Object getResult() {
		return this.result;
	}

	@Override
	public String getQueryString() {
		return this.query.query;
	}
	
}
