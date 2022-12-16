package com.alienarnchy.sql.database;

/**
 * @note Just a simple data holder.
 *  
 * @author Tiago Ramos
 */
public class RemoteDatabase implements Database {
	
	public String address, username, password;
	private DatabaseType type;
	
	/**
	 * RemoteDatabase
	 * 
	 * Note: db.type = Remote
	 * 
	 * @param addr address to database
	 * @param usnm username to log in
	 * @param pwd password to log in
	 */
	public RemoteDatabase(String addr, String usnm, String pwd) {
		this.address = addr;
		this.username = usnm;
		this.password = pwd;
		this.type = DatabaseType.Remote;
	}

	@Override
	public DatabaseType getType() {
		return this.type;
	}
	
}
