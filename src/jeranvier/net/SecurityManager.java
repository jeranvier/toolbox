package jeranvier.net;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class SecurityManager {
	
	private String canonicalRoot;
	
	public SecurityManager() throws IOException{
		canonicalRoot = new File(HTTPServer.getPreference("root")).getCanonicalPath();
	}
	
	public boolean isRequestedPathUnderRoot(URI uri) throws IOException{
		String canonicalPath = new File(HTTPServer.getPreference("root")+uri.getPath()).getCanonicalPath();		
		if(canonicalPath.startsWith(canonicalRoot)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isURITooLarge(URI uri){
		return uri.getRawPath().length() <= Integer.parseInt(HTTPServer.getPreference("maxSizeURL", "2048"));
	}
}
