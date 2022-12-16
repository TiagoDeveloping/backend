package database.queries;

import java.sql.SQLException;
import java.util.ArrayList;

import com.alienarnchy.sql.ManagedQuery;
import com.alienarnchy.sql.SqlUpdate;
import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

public class InsertQuery implements ManagedQuery {

	public SqlUpdate command;
	
	private String[] keys, values, elements;
	private String tableName;
	
	private int result;
	
	private static final String query = "INSERT INTO {0} ({1}) VALUES({2});";
	
	/**
	 * InserQuery
	 * 
	 * @param elements array for the query 
	 * @note {0} table to insert to
	 * @note {1} names of the collums (surrounded by `)
	 * @note {2} values to be inserted (surrounded by `)
	 * 
	 * @see String a[] = {"users", [`username`, `uid`, `posts`], ['tiago', '0', '0']};
	 * 
	 * @param db the database
	 */
	public InsertQuery(String tableName, String[] keys, String[] values, Database db) {
		this.tableName = tableName;
		this.keys = keys;
		this.values = values;
		
		prepareQuery();
		
		command = new SqlUpdate(query, elements, db);
	}
	
	private void prepareQuery() {
		ArrayList<String> arr = new ArrayList<String>();
		
		arr.add(this.tableName);
		
		String keysForQuery = keyArrayToString();
		arr.add(keysForQuery);
		
		String valuesForQuery = valueArrayToString();
		arr.add(valuesForQuery);
		
		String[] str = new String[3];
		
		for (int i = 0; i < arr.size(); i++) {
			str[i] = arr.get(i);
		}
		
		elements = str;
	}

	private String keyArrayToString() {
		String keysForQuery = "";
		for (int i = 0; i < this.keys.length; i++) {
			if (i > 0) {
				keysForQuery += (", `" + this.keys[i] + "`");
			} else {
				keysForQuery += ("`" + this.keys[i] + "`");
			}
		}
		return keysForQuery;
	}
	
	private String valueArrayToString() {
		String valuesForQuery = "";
		for (int i = 0; i < this.values.length; i++) {
			if (i > 0) {
				valuesForQuery += (", '" + this.values[i] + "'");
			} else {
				valuesForQuery += ("'" + this.values[i] + "'");
			}
		}
		return valuesForQuery;
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

	@Override
	public Object getResult() {
		return this.result;
	}
	

}
