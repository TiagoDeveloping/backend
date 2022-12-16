package backend.alienanarchy.main;

import java.io.IOException;
import java.util.Scanner;

public class ServerCommandHandlingThread implements Runnable {

	@Override
	public void run() {
		Scanner sysin = new Scanner(System.in);
		String cmd;
		while ((cmd = sysin.nextLine()) != null) {
			switch (cmd.split(" ")[0]) {
			case "exit":
				sysin.close();
				exit();
			case "?":
				help();
				break;
			}
		}
	}
	
	private void help() {
		System.out.println("Help page: \n"
				+ "? : shows this page \n"
				+ "exit : stops whole program \n"
				+ ""
				);
	}

	private void exit() {
		try {
			Main.secrets.save();
		} catch (IOException e) {
			Main.errorLog.getStream().println(Main.secrets.toString()); // dump property state to log for optional recovery
			e.printStackTrace();
			System.err.println("Property content dumped to log");
		}
		Main.errorLog.closeStream();
		Main.sqlLog.closeStream();
		System.exit(0);
	}
}
