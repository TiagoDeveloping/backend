package com.alienanarchy.file.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;

import com.alienanarchy.file.ManagedFile;

/**
 * Allows downloading of a remote file.
 * 
 * @author Tiago Ramos
 * @since beginning
 */
public class ManagedRemoteFile implements ManagedFile {

	private File file;
	private Path path;
	private URL url;
	
	private boolean isTemporary = false;
	
	private String fileName;
	
	/**
	 * RemoteFile
	 * 
	 * Note: always considered non-temporary file unless specified otherwise.
	 * 
	 * @param urlToWebContent url to the web file.
	 * @param fileName name of file to be written.
	 * @param path path to directory for file.
	 */
	public ManagedRemoteFile(URL urlToWebContent, String fileName, Path path) {
		file = new File(path.toString() + "/" + fileName);
		this.url = urlToWebContent;
		this.fileName = fileName;
		
		this.path = path;
	}
	
	/**
	 * RemoteFile
	 * 
	 * Note: always considered non-temporary file unless specified otherwise.
	 * 
	 * @param urlToWebContent url to the web file.
	 * @param fileName name of file to be written.
	 * @param path path to directory for file.
	 * @param isTemporary whether the file should be downloaded for temporary access
	 */
	public ManagedRemoteFile(URL urlToWebContent, String fileName, Path path, boolean isTemporary) {
		file = new File(path.toString() + "/" + fileName);
		this.url = urlToWebContent;
		this.fileName = fileName;
		this.isTemporary = isTemporary;
		
		this.path = path;
	}
	
	/**
	 * Transfers the data from the URL to the local file.
	 * 
	 * @throws FileNotFoundException when the file to be written to doesn't exist yet.
	 * @throws IOException if I/O error occurs during downloading process.
	 */
	public void transferToFile() throws IOException, FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException("The file to be written to doesn't exist yet.");
		}
		InputStream in = new BufferedInputStream(url.openStream());
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

		for (int i; (i = in.read()) != -1; ) {
		    out.write(i);
		}
		
		in.close();
		out.close();
	}
	
//	/**
//	 * @return whether the file from filename already exists.
//	 */
	public boolean exists() {
		return this.file.exists();
	}
	
//	/**
//	 * Creates the file.
//	 * 
//	 * @throws SecurityException when not allowed to create the file.
//	 * @throws IOException when the file could not be created.
//	 */
	public void createFile() throws IOException, SecurityException {
		if (this.isTemporary) {
			File.createTempFile(getFilePrefix(), getFileSuffix(), file);
		} else {
			this.file.createNewFile();
		}
	}
	
	@Override
	public File getFile() {
		return file;
	}
	@Override
	public void deleteFile() throws IOException {
		file.delete();
	}
	private String getFileSuffix() {
		return this.fileName.split(".")[1];
	}
	
	private String getFilePrefix() {
		return this.fileName.split(".")[0]; 
	}

	@Override
	public Path getPath() {
		return this.path;
	}


}






