package jeranvier.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jeranvier.net.example.DefaultHTTPHandler;
import jeranvier.net.exceptions.ErrorHandler;
import jeranvier.prefs.Preferences;

public class HTTPServer {
	
	
	public enum SERVER_STATUS{STARTING, RUNNING, STOPPING, STOPPED}
	private SERVER_STATUS status;
	private static ErrorHandler errorHandler;
	private static final Preferences globalPrefs = new Preferences("config/server.prefs");
	private static final DateFormat rfc1123Format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	private static SecurityManager securityManager;
	
	public HTTPServer(int port, InetAddress bindAddr) throws IOException{
		createServer(port, Integer.MAX_VALUE, bindAddr, DefaultHTTPHandler.class);
	}
	
	public HTTPServer(int port) throws IOException{
		createServer(port, Integer.MAX_VALUE, InetAddress.getLocalHost(), DefaultHTTPHandler.class);
	}
	
	public HTTPServer(int port, InetAddress byName, Class<? extends HTTPHandler> HTTPHandlerClass) throws IOException{
		createServer(port, Integer.MAX_VALUE, byName, HTTPHandlerClass);
	}

	public void createServer(int port, int maxConnections, InetAddress bindAddr, Class<? extends HTTPHandler> HTTPHandlerClass) throws IOException{
		this.status = SERVER_STATUS.STARTING;
		
		securityManager = new SecurityManager();
		errorHandler = new ErrorHandler();
		
		@SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(port, maxConnections, bindAddr);
		System.out.println("Server listening on: "+bindAddr.getHostAddress()+":"+port);
		this.status = SERVER_STATUS.RUNNING;
		while(status == SERVER_STATUS.RUNNING){
			Socket clientSocket = socket.accept();
			try {
				HTTPHandler handler = HTTPHandlerClass.newInstance();
				handler.setSocket(clientSocket);
				new Thread(handler).start();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException{
		HTTPServer server = new HTTPServer(8080, InetAddress.getByName("128.179.158.81"),DefaultHTTPHandler.class);
	}
	
	public static String getDateRFC1123(){
		return rfc1123Format.format(new Date());
	}
	
	public static String getPreference(String key, String defaultValue){
		return globalPrefs.getPreference(key, defaultValue);
	}
	
	public static String getPreference(String key){
		return globalPrefs.getPreference(key);
	}
	
	public static ErrorHandler getErrorHandler(){
		return errorHandler;
	}
	
	public static SecurityManager getSecurityManager(){
		return securityManager;
	}

}
