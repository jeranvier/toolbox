package jeranvier.net;

import java.util.HashMap;
import java.util.Map;

public class HttpStates {
	Map<Integer, String> states;
	
	public HttpStates(){
		states = new HashMap<Integer, String>();
		states.put(200, "200 OK");
		states.put(201, "201 Created");
		states.put(202, "202 Accepted");
		states.put(204, "204 No Content");
		states.put(304, "304 Not Modified");
		states.put(400, "400 Bad Request");
		states.put(401, "401 Unauthorized");
		states.put(403, "403 Forbidden");
		states.put(404, "404 Not Found");
		states.put(405, "405 Method Not Allowed");
		states.put(406, "406 Not Acceptable");
		states.put(500, "500 Internal Server Error");
		states.put(501, "501 Not Implemented");
		states.put(503, "503 Service Unavailable");
	}
	
	public String getState(int code){
		String state = this.states.get(code);
		return state != null? state : this.states.get(500);
	}
	
	public int getCode(String state){
		return states.containsValue(state)? Integer.parseInt(state.substring(0, 3)) : null;
	}
}
