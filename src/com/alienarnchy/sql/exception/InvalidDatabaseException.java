package com.alienarnchy.sql.exception;

/**
 * 
 * @author Tiago Ramos
 *
 */
public class InvalidDatabaseException extends Exception {

	private static final long serialVersionUID = -5259296093225470437L;

	/**
	 * @param msg to specify error
	 */
	public InvalidDatabaseException(String msg) {
		super(msg);
	}
}
