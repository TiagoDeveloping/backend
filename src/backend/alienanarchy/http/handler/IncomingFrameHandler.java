package backend.alienanarchy.http.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import backend.alienanarchy.main.Main;
import database.queries.PostQuery;
import database.queries.RegisterQuery;

/**
 * 
 * @author Tiago Ramos
 *
 */
public class IncomingFrameHandler extends AbstractHandler {

	@Override
	public void handle(String target, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
		try {
			HashMap<String, String> queryMap = parseRequestQuery(httpRequest.getQueryString());
			
			String type = (String) queryMap.get("type");
			
			switch(type) {
			case "getPost":
				doGetPost(httpResponse, queryMap, "getPost");
				break;
			case "registerUser":
				doCreateUser(httpResponse, queryMap, "registerUser");
				break;
			}		
			
		} catch (NullPointerException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
			request.setHandled(false);
		} catch (IOException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
			request.setHandled(false);
		}
		
		request.setHandled(true);
	}

	private void doCreateUser(HttpServletResponse httpResponse, HashMap<String, String> queryMap, String string) throws IOException {
//		try {
//			String[] keys =  {"username", "password", "email", "id", "post_ids"}; // TODO maybe CreateUserQuery
//			String[] values = {queryMap.get(keys[0]), queryMap.get(keys[1]), queryMap.get(keys[2]), queryMap.get(keys[3]), queryMap.get(keys[4])};
//					
//			InsertQuery insert = new InsertQuery("users", keys, values, Main.mainDatabase);
//			insert.execute();
//			httpResponse.setContentType("text/plain");
//			httpResponse.getWriter().write(insert.execute() + "");
//		} catch (NullPointerException e) {
//			e.printStackTrace(Main.errorLog.getStream());
//			System.err.println(e.getMessage());
//			return;
//		}
		
		RegisterQuery rq = new RegisterQuery(queryMap.get("username"), queryMap.get("password"), queryMap.get("email"), UUID.fromString(queryMap.get("id")), Main.mainDatabase);
		try {
//			rq.execute();
			httpResponse.setContentType("text/plain");
			httpResponse.getWriter().write(rq.execute() + "");
		} catch (NullPointerException e) {
			e.printStackTrace(Main.errorLog.getStream());
			System.err.println(e.getMessage());
			return;
		}
	}

	private void doGetPost(HttpServletResponse httpResponse, HashMap<String, String> queryMap, String key) throws IOException {
		PostQuery query = new PostQuery(queryMap.get(key), Main.mainDatabase);
		query.execute();
		String content = query.getResult();
		
		httpResponse.setContentType("text/plain");
		httpResponse.getWriter().write(content); 
	}
	
	private HashMap<String, String> parseRequestQuery(String queryString) throws NullPointerException {
		if (queryString.length() < 1) throw new NullPointerException("Empty query request");
		HashMap<String, String> queryMap = new HashMap<String, String>();
		String[] queries = queryString.split("&");
		
		for (String s : queries) {
			String key = s.split("=")[0];
			String value = s.split("=")[1];
			queryMap.put(key, value);
		}
		
		return queryMap;
	}
	
}
