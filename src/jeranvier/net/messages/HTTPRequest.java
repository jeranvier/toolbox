package jeranvier.net.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import jeranvier.net.exceptions.HTTPException;

public class HTTPRequest {
	
	public static enum HTTPMethod {PUT, POST, GET, DELETE, NOT_HTTP_METHOD, ERROR}

	private HTTPMethod method = HTTPMethod.NOT_HTTP_METHOD;
	private URI uri;
	private String HTTPVersion;
	private String body;
	private Map<String, String> header;
	private InputStream input;

	public HTTPRequest(InputStream input) throws IOException, HTTPException, URISyntaxException {
		this.input = input;
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String line = "";
		String firstLine = in.readLine();
		String[] firstLineSplit;
		if(firstLine != null && (firstLineSplit=firstLine.split(" ")).length == 3){
			this.method = HTTPMethod.valueOf(firstLineSplit[0]);
			this.uri = new URI(firstLineSplit[1]);
			this.HTTPVersion = firstLineSplit[2];
		}
		else{
			throw new HTTPException();
		}
		
		header = new HashMap<String, String>();
		while((line = in.readLine()) != null){
			if(line.length() == 0){
				break;
			}
			else{
				String[] lineSplit = line.split(": ");
				header.put(lineSplit[0], lineSplit[1]);
			}
		}
		
		if(in.ready()){ //their is content
			StringBuilder bodyBuilder = new StringBuilder();
			while((line = in.readLine()) != null){
				bodyBuilder.append(line);
				bodyBuilder.append("\r\n");
			}
			body = bodyBuilder.toString();
		}
	}
	
	public void closeStream() throws IOException{
		input.close();
	}

	public HTTPMethod getMethod() {
		return this.method;
	}

	public URI getPath() {
		return uri;
	}

	public String getHTTPVersion() {
		return HTTPVersion;
	}
	
	public String getBody(){
		return this.body;
	}

	public Map<String, String> getHeader() {
		return header;
	}

}
