package jeranvier.net.exceptions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import jeranvier.io.FileLoader;
import jeranvier.net.Document;
import jeranvier.net.HTTPServer;
import jeranvier.net.messages.HTTPRequest;
import jeranvier.net.messages.HTTPResponse;

public class ErrorHandler {

	public ErrorHandler(){
	}
	
	public void handleError(int errorCode, HTTPRequest request, HTTPResponse  response){
		String errorPagePath = HTTPServer.getPreference("errorsPath")+"/"+errorCode+".html";
		File file = new File(errorPagePath);
		if(! (file.isFile() || file.canRead())){
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
}
