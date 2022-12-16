package com.alienarnchy.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.database.RemoteDatabase;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

public class SqlUpdate {

	public String query;
	private String[] elements;
	private Database db;
	
	private Statement stm;
	Connection connection;
	
	/**
	 * Command that doesn't return ResultSet.
	 * 
	 * Example : CREATE TABLE table_name (column1, column2);
	 * where {0} is i=0 for elements array.
	 * 
	 * @param globalCommand the query with the {0} elements
	 * @param elements array that specifies the values of {0} until {i}
	 */
	public SqlUpdate(String globalQuery, String[] elements, Database db) {
		this.db = db;
		
		this.query = globalQuery;
		this.elements = elements;
		
		parseQuery(globalQuery, elements);
	}

	private void parseQuery(String globalQuery, String[] elements) {
		for (int i = 0; i < elements.length; i++) {
			query = query.replace("{" + i + "}", elements[i]);
		}
	}
	
	/**
	 * Executes the command.
	 * 
	 * Note: Loads url, username and password from database given in constructor.
	 * Note: Will only work if Database.type != specified and is initialized
	 * Note: doesn't close connection
	 * 
	 * @throws SQLException when something goes wrong in the sql dept.
	 * @throws InvalidDatabaseException when database invalid
	 * @throws IOException when unable to log
	 */
	public int execute() throws SQLException, InvalidDatabaseException {
		switch (db.getType()) {
		case Remote:
			RemoteDatabase rmdb = (RemoteDatabase) db;
			this.connection = DriverManager.getConnection(rmdb.address, rmdb.username, rmdb.password);
			
			this.stm = connection.createStatement();
			
			Main.sqlLog.getStream().println( (LocalDateTime.now().toString()) + "  :  " + query);
			return stm.executeUpdate(this.query);
		case Unspecified:
			throw new InvalidDatabaseException("Database type unspecified");
		default:
			throw new InvalidDatabaseException("No database type declared");
		}
	}

	/**
	 * Loads a new sql command.
	 * 
	 * @param queryCommand new queryCommand to be used.
	 * @param elements new elements for the query.
	 */
	public void setCommand(String queryCommand, String[] elements) {
		parseQuery(queryCommand, elements);
	}
	
	/**
	 * Get the current replacement elements for the query
	 * 
	 * @return String[] of replacement elements for query
	 */
	public String[] getElements() {
		return this.elements;
	}
	
	/**
	 * Closes all open structures.
	 * 
	 * @throws SQLException when something goes wrong in the sql dept.
	 */
	public void close() throws SQLException {
		connection.close();
		stm.close();
	}

}
