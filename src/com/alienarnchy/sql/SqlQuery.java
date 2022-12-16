package com.alienarnchy.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import com.alienarnchy.sql.database.Database;
import com.alienarnchy.sql.database.RemoteDatabase;
import com.alienarnchy.sql.exception.InvalidDatabaseException;

import backend.alienanarchy.main.Main;

public class SqlQuery {

	public String query;
	private ResultSet result;
	private String[] elements;
	private Database db;
	
	private Statement stm;
	Connection connection;
	
	/**
	 * Query request.
	 * 
	 * Example query: SELECT `password` FROM `users` WHERE `username`=\"{0}\"
	 * where {0} is i=0 for elements array.
	 * 
	 * @param globalQuery the query with the {0} elements
	 * @param elements array that specifies the values of {0} until {i}
	 * @param db database to use
	 */
	public SqlQuery(String globalQuery, String[] elements, Database db) {
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
	 * Query request.
	 * 
	 * Example query: SELECT `password` FROM `users` WHERE `username`=\"{0}\"
	 * where {0} is i=0 for elements array.
	 * 
	 * @param query the query with the {0} elements
	 * @param db database to use
	 */
	public SqlQuery(String query, Database db) {
		this.db = db;
		this.query = query;
	}
	
	/**
	 * Executes the query.
	 * 
	 * Note: Loads url, username and password from database given in constructor.
	 * Note: Will only work if Database.type != specified and is initialized
	 * Note: doesn't close connection
	 * 
	 * @throws SQLException when something goes wrong in the sql dept.
	 * @throws InvalidDatabaseException when database invalid
	 */
	public void execute() throws SQLException, InvalidDatabaseException {
		switch (db.getType()) {
		case Remote:
			RemoteDatabase rmdb = (RemoteDatabase) db;
			this.connection = DriverManager.getConnection(rmdb.address, rmdb.username, rmdb.password);
			
			this.stm = connection.createStatement();
			
			this.result = stm.executeQuery(this.query);

			Main.sqlLog.getStream().println( (LocalDateTime.now().toString()) + "  :  " + query);
			break;
		case Unspecified:
			throw new InvalidDatabaseException("Database type unspecified");
		default:
			throw new InvalidDatabaseException("No database type declared");
		};
	}
	
	/**
	 * Executes the query.
	 * 
	 * Note: Loads url, username and password from database given in constructor.
	 * Note: Will only work if Database.type != specified and is initialized
	 * 
	 * @param closeConnection if the connection to the database should close or not.
	 * 
	 * @throws SQLException when something goes wrong in the sql dept.
	 * @throws InvalidDatabaseException when database invalid
	 */
	public void execute(boolean closeConnection) throws SQLException, InvalidDatabaseException {
		switch (db.getType()) {
		case Remote:
			RemoteDatabase rmdb = (RemoteDatabase) db;
			this.connection = DriverManager.getConnection(rmdb.address, rmdb.username, rmdb.password);
			
			this.stm = connection.createStatement();
			
			this.result = stm.executeQuery(this.query);
			Main.sqlLog.getStream().println( (LocalDateTime.now().toString()) + "  :  " + query);
			
			if (closeConnection) connection.close();
			break;
		case Unspecified:
			throw new InvalidDatabaseException("Database type unspecified");
		default:
			throw new InvalidDatabaseException("No database type declared");
		}
	}
	
	/**
	 * Loads a new query command.
	 * 
	 * @param queryCommand new queryCommand to be used.
	 * @param elements new elements for the query.
	 */
	public void setQuery(String queryCommand, String[] elements) {
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
	 * Get the of the last executed query result.
	 * 
	 * @return ResultSet the result of the query.
	 */
	public ResultSet getResult() {
		return this.result;
	}
	
	/**
	 * Closes all open structures.
	 * 
	 * @throws SQLException when something goes wrong in the sql dept.
	 */
	public void close() throws SQLException {
		connection.close();
		stm.close();
		result.close();
	}
	
	
}
