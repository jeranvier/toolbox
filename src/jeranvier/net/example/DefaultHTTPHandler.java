package jeranvier.net.example;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import jeranvier.io.FileLoader;
import jeranvier.net.Document;
import jeranvier.net.HTTPHandler;
import jeranvier.net.HTTPServer;
import jeranvier.net.messages.HTTPRequest;
import jeranvier.net.messages.HTTPResponse;

public class DefaultHTTPHandler extends HTTPHandler{

	@Override
	protected void handlePost(HTTPRequest request, HTTPResponse response) {
		displayRequest(request);
	}

	@Override
	protected void handlePut(HTTPRequest request, HTTPResponse response) {
		displayRequest(request);
		
	}

	@Override
	protected void handleDelete(HTTPRequest request, HTTPResponse response) {
		displayRequest(request);
		
	}

	@Override
	protected void handleGet(HTTPRequest request, HTTPResponse response) {
		displayRequest(request);
		
		File file = new File(HTTPServer.getPreference("root")+request.getPath().getPath());
		
		if(! file.isFile()){
			response.setHTTPCode(404);
			return;
		}
		
		if(! file.canRead()){
			response.setHTTPCode(401);
			return;
		}
		
		try {
			FileLoader<Document> fl = new FileLoader<Document>(file.getCanonicalPath(), Document.class);
			response.write(fl.loadFileInObject().getContent());
		} catch (IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void displayRequest(HTTPRequest request) {
		System.out.println(request.getMethod()+" "+request.getPath()+" "+request.getHTTPVersion());
		for(Entry<String, String>  headerEntry : request.getHeader().entrySet()){
			System.out.println(headerEntry.getKey()+": "+headerEntry.getValue());
		}
		System.out.println("");
		if(request.getBody()!= null){
			System.out.println(request.getBody());
		}
	}
}
