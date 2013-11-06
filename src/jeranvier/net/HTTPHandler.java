package jeranvier.net;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

import jeranvier.net.exceptions.HTTPException;
import jeranvier.net.messages.HTTPRequest;
import jeranvier.net.messages.HTTPResponse;

public class HTTPHandler implements Runnable{

	private Socket socket = null;
	
	public HTTPHandler(){
    	/*
    	 * STUB
    	 */
    }
    
	protected void setSocket(Socket socket){
		this.socket = socket;
    	System.out.println("Handling connexion from: "+socket.getInetAddress().getHostAddress());
	}
	
    public void run() {
        try {
            HTTPRequest request = new HTTPRequest(socket.getInputStream());
            HTTPResponse  response = new HTTPResponse(socket.getOutputStream(), request.getHeader());
            if(! HTTPServer.getSecurityManager().isRequestedPathUnderRoot(request.getPath())){
            	response.setHTTPCode(404);
            }
            System.out.println("been here");
            switch(request.getMethod()){
            case POST: this.handlePost(request, response); break;
            case PUT: this.handlePut(request, response); break;
            case GET: this.handleGet(request, response); break;
            case DELETE: this.handleDelete(request, response); break;
			default:
				break;
            }
            response.sendResponse();
            response.closeStream();
            request.closeStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HTTPException e) {
			System.out.println("Received a non HTTP request from: "+socket.getInetAddress().getHostAddress());
		} catch (URISyntaxException e) {
			System.out.println("Received a wrong URI from: "+socket.getInetAddress().getHostAddress());
		}
       finally{
           try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       }
    }
    
    protected void handlePost(HTTPRequest request, HTTPResponse  response){ throw new UnsupportedOperationException();}
    protected void handlePut(HTTPRequest request, HTTPResponse  response){throw new UnsupportedOperationException();}
    protected void handleDelete(HTTPRequest request, HTTPResponse  response){throw new UnsupportedOperationException();}
    protected void handleGet(HTTPRequest request, HTTPResponse  response){throw new UnsupportedOperationException();}
}
