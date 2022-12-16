package com.alienanarchy.file.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import com.alienanarchy.file.ManagedFile;

/**
 * 
 * @author Tiago Ramos
 *
 */
public class ManagedLogFile implements ManagedFile {

	private File logFile;
	private Path filePath;
	private boolean keepContent = false;
	
	private PrintStream stream;
	
	/**
	 * ManagedLogFile.
	 * 
	 * Manages simple stream for a log.
	 * 
	 * @param fileName name of file
	 * @param path path to directory for file
	 * 
	 * @throws FileNotFoundException, IOException when shit happens
	 */
	public ManagedLogFile(String fileName, Path path) {
		logFile = new File(path.toString() + "/" + fileName);
		this.filePath = path;
	}
	
	/**
	 * ManagedLogFile.
	 * 
	 * Manages simple stream for a log.
	 * 
	 * @param fileName name of file
	 * @param path path to directory for file
	 * 
	 * @throws FileNotFoundException, IOException when shit happens
	 */
	public ManagedLogFile(String fileName, Path path, boolean keepContent) {
		logFile = new File(path.toString() + "/" + fileName);
		this.filePath = path;
		
		this.keepContent = keepContent;
	}
	
	
	/**
	 * Initiates the stream.
	 * 
	 * @throws FileNotFoundException when log file not found.
	 */
	public void initiateStream() throws FileNotFoundException, IOException {
		if (keepContent) {
			FileInputStream in = new FileInputStream(logFile);
			
			byte[] data = in.readAllBytes();

			in.close();
			
			stream = new PrintStream(logFile);
			stream.append(new String(data));
		} else {
			stream = new PrintStream(logFile);
		}
	}
	
	/**
	 * Gets the stream to the log file.
	 * 
	 * Note: stream needs to be initiated first.
	 * 
	 * @return stream to log file
	 */
	public PrintStream getStream() {
		return stream;
	}
	
	/**
	 * Closes the stream.
	 */
	public void closeStream() {
		stream.close();
	}
	
	@Override
	public boolean exists() {
		return logFile.exists();
	}

	@Override
	public void createFile() throws IOException, SecurityException {
		logFile.createNewFile();
	}

	@Override
	public void deleteFile() throws IOException {
		logFile.delete();
	}

	@Override
	public File getFile() {
		return logFile;
	}
	
	@Override
	public Path getPath() {
		return this.filePath;
	}

}
