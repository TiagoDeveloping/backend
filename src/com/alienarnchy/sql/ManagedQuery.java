package com.alienarnchy.sql;

public interface ManagedQuery {

	/**
	 * Executes the queryCommand.
	 * 
	 * @return 1 -> succeeded
	 * @return -1 -> error occured 
	 */
	public int execute();
	
	/**
	 * 
	 * @return Result.
	 */
	public Object getResult();
	
	/**
	 * 
	 * @return QueryString
	 */
	public String getQueryString();
	
}
