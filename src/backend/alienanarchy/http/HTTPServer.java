package backend.alienanarchy.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.ThreadPool;

import backend.alienanarchy.http.handler.IncomingFrameHandler;
import backend.alienanarchy.main.Main;
import tree.Node;

/**
 * 
 * @author Tiago Ramos
 *
 */
public class HTTPServer extends Server {
	
	public static Node userTree;
	
	private ServerConnector connector;
	private AbstractHandler handler;
	
	public HTTPServer(ThreadPool pool) {
		super(pool);
		
		connector = new ServerConnector(this);
		connector.setHost(Main.secrets.read("host"));
		connector.setPort(Integer.parseInt(Main.secrets.read("port")));
		
		this.addConnector(connector);
		
		handler = new IncomingFrameHandler();
		this.setHandler(handler);
	}
	
	public void startServer() throws Exception {
		this.start();
		connector.start();
		handler.start();
	}
	
	public void stopServer() throws Exception {
		this.stop();
		connector.stop();
		handler.stop();
	}
	
}
