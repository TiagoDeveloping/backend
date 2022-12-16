package com.alienanarchy.file.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Properties;

import com.alienanarchy.file.ManagedFile;

/**
 * 
 * @author Tiago Ramos
 *
 */
public class ManagedPropertyFile implements ManagedFile {

	private File propertyFile;
	@SuppressWarnings("unused")
	private String propertyFileName;
	@SuppressWarnings("unused")
	private Path path;
	
	private Properties properties;
	
	/**
	 * ManagedPropertyFile
	 * 
	 * @param propertyFileName name of file e.g: config.properties
	 * @param dataFolderPath path to app data folder
	 */
	public ManagedPropertyFile(String propertyFileName, Path dataFolderPath) {
		this.propertyFile = new File(dataFolderPath.toString() + "/" + propertyFileName);
		this.propertyFileName = propertyFileName;
	}
	
	/**
	 * Loads property file into memory.
	 * 
	 * @throws IOException when e.g. file not found.
	 */
	public void loadProperty() throws IOException {
		FileInputStream in = new FileInputStream(propertyFile);
		Properties properties = new Properties();
		
		properties.load(in);
		this.properties = properties;
		
		in.close();
	}
	
	/**
	 * Clears properties from memory
	 */
	public void clear()  {
		properties.clear();
	}
	
	/**
	 * Saves the file.
	 * Note: Need to load again after save(clears memory)
	 * 
	 * Recommended when not in use for long time.
	 * 
	 * @throws IOException when file cound't be saved.
	 */
	public void save() throws IOException {
		OutputStream out = new FileOutputStream(propertyFile);

		properties.store(out, "@author Tiago Ramos");
		
		out.close();
		
		properties.clear();
	}
	
	/**
	 * Saves the file.
	 * 
	 * Recommended when not in use for long time.
	 * 
	 * @param unload whether properties should be loaded out of memory
	 * 
	 * @throws IOException when file cound't be saved.
	 */
	public void save(boolean unload) throws IOException {
		OutputStream out = new FileOutputStream(propertyFile);

		properties.store(out, "@author Tiago Ramos");
		
		out.close();
		
		if (!unload) return;
		properties.clear();
	}
	
	public Path getPath() {
		return this.propertyFile.toPath();
	}
	
	@Override
	public void deleteFile() throws SecurityException {
		propertyFile.delete();
	}
	
	public String read(String key) {
		String data = "";
		data = properties.getProperty(key);
		return data;
	}

	public void put(String key, String value) throws IOException {
		properties.setProperty(key, value);
	}

	@Override
	public boolean exists() {
		return this.propertyFile.exists();
	}
	
	@Override
	public void createFile() throws IOException, SecurityException {
		this.propertyFile.createNewFile();
	}

	@Override
	public File getFile() {
		return propertyFile;
	}
}
