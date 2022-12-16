 package backend.alienanarchy.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.alienanarchy.file.log.ManagedLogFile;
import com.alienanarchy.file.property.ManagedPropertyFile;
import com.alienarnchy.sql.database.RemoteDatabase;

import backend.alienanarchy.http.HTTPServer;
import main.Parser;

/**
 * @author Tiago Ramos
 */
public class Main {
	
	public static Path path = FileSystems.getDefault().getPath(".");
	
	public static ManagedLogFile errorLog = new ManagedLogFile("/data/log.log", path);
	public static ManagedLogFile sqlLog = new ManagedLogFile("/data/sql.log", path, true);
	
	public static ManagedPropertyFile secrets = new ManagedPropertyFile("/data/secrets.properties", path);
	
	public static Parser dataFileParser = new Parser();
	
	public static RemoteDatabase mainDatabase;
	
	public static void main(String[] args) {
		setup();
		
		HTTPServer httpServer = new HTTPServer(new QueuedThreadPool());
		
		Thread commandHandler = new Thread(new ServerCommandHandlingThread());
		
		try {
			httpServer.startServer();
			commandHandler.start();
		} catch (IllegalThreadStateException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
			
			commandHandler.interrupt();
			commandHandler.start(); // restart??
		} catch (Exception e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
		}
		
		smollstuff();
	}

	private static void setup() {
		try {
			if (!errorLog.exists()) errorLog.createFile();
			errorLog.initiateStream();
			
			if (!sqlLog.exists()) sqlLog.createFile();
			sqlLog.initiateStream();
			
			if (secrets.exists()) {
				secrets.loadProperty();
			} else {
				throw new FileNotFoundException("Couldn't find property file.");
			}
			
			mainDatabase = new RemoteDatabase(secrets.read("sql_url"), secrets.read("sql_username"), secrets.read("sql_password"));
			
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
	}

	private static void smollstuff() {
		
	}
}
