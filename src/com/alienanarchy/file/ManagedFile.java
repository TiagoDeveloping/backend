package com.alienanarchy.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * @author Tiago Ramos
 *
 */
public interface ManagedFile {
// make constructor with just path for all file types.

	/**
	 * @return whether the file from filename already exists.
	 */
	public boolean exists();
	
	/**
	 * Creates the file.
	 * 
	 * @throws SecurityException when not allowed to create the file.
	 * @throws IOException when the file could not be created.
	 */
	public void createFile() throws IOException, SecurityException;
	
	/**
	 * Deletes the file
	 * @throws IOException when the file could not be deleted.
	 */
	public void deleteFile() throws IOException;
	
	/**
	 * @return the concerning file
	 */
	public File getFile();
	
	/**
	 * @return path to the enclosing folder of the file
	 */
	public Path getPath();
}
