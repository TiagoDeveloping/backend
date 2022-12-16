package database.queries;

import java.sql.SQLException;
import java.util.UUID;

import com.alienarnchy.sql.SqlUpdate;
import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

public class RegisterQuery {
	
	public SqlUpdate command;
	
	private String username, password, email;
//	private String tableName = "users";
	private UUID uid;
	
	private String[] elements; 
	
	private int result;
	
	private static final String query = "INSERT INTO users (`username`, `password`, `email`, `uid`) VALUES({0});";
	
	/**
	 * InserQuery
	 * 
	 * @param elements array for the query 
	 * @note {0} table to insert to
	 * @note {1} names of the collums (surrounded by `)
	 * @note {2} values to be inserted (surrounded by `)
	 * 
	 * @see String a[] = {"users", "`username`, `uid`, `posts`", "'tiago', '0', '0'"};
	 * 
	 * @param db the database
	 */
	public RegisterQuery(String username, String password, String email, UUID uid, Database db) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.uid = uid;
		
		prepareQuery();
		
		command = new SqlUpdate(query, elements, db);
	}
	
	private void prepareQuery() {
		String[] str = new String[1];
		
		
		String a[] = new String[4];
		a[0] = username;
		a[1] = password;
		a[2] = email;
		a[3] = uid.toString();
		
		str[0] = valueArrayToString(a);
		
		elements = str;
	}

	private String valueArrayToString(String[] arr) {
		String keys = "";
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				keys += (", '" + arr[i] + "'");
			} else {
				keys += ("'" + arr[i] + "'");
			}
		}
		return keys;
	}
	
	public String getQueryString() {
		return this.command.query;
	}
	
	/**
	 * Executes the query.
	 * @return -1 if failed. Everything else idk what it means but it probably either fucked up your db or worked ;)
	 */
	public int execute() {
		try {
			this.result = command.execute();
			return result;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(Main.errorLog.getStream());
			this.result = -1;
			return -1;
		} catch (InvalidDatabaseException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(Main.errorLog.getStream());
			this.result = -1;
			return -1;
		}
	}

	public Object getResult() {
		return this.result;
	}
	
}
