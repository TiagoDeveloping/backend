package database.queries;

import java.sql.SQLException;
import java.util.ArrayList;

import com.alienarnchy.sql.ManagedQuery;
import com.alienarnchy.sql.SqlUpdate;
import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

// TODO Improve usability --> cp from insertQuery
public class CreateTableQuery implements ManagedQuery {

	public SqlUpdate command;
	
	private String[] elements, collumNames;
	private String name;
	
	private static final String query = "CREATE TABLE `{0}` ({1});";
	
	private int result;
	
	/**
	 * CreateTableQuery
	 * 
	 * @param [elements] array for the query 
	 * @param {0} table to insert to
	 * @param {1} names of the collums (surrounded by `)
	 * 
	 * @param db the database
	 */
	public CreateTableQuery(String name, String[] collumNames, Database db) {
		this.collumNames = collumNames;
		this.name = name;
		
		prepareQuery();
		
		command = new SqlUpdate(query, elements, db);
	}
	
	private void prepareQuery() {
		ArrayList<String> arr = new ArrayList<String>();
		
		arr.add(this.name);
		
		String collumString = collumnNameArrayToString();
		arr.add(collumString);
		
		String[] str = new String[2];
		
		for (int i = 0; i < arr.size(); i++) {
			str[i] = arr.get(i);
		}
		
		elements = str;
	}
	
	private String collumnNameArrayToString() {
		String collums = "";
		for (int i = 0; i < this.collumNames.length; i++) {
			if (i > 0) {
				collums += (", `" + this.collumNames[i].split(" ")[0] + "` " + this.collumNames[i].split(" ")[1]);
			} else {
				collums += ("`" + this.collumNames[i].split(" ")[0] + "` " + this.collumNames[i].split(" ")[1]);
			}
		}
		return collums;
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

	@Override
	public String getQueryString() {
		return this.command.query;
	}
	

}
