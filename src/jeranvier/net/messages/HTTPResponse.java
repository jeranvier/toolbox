package jeranvier.net.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import jeranvier.net.HTTPServer;
import jeranvier.net.HttpStates;

public class HTTPResponse {

	private OutputStream output;
	private Map<String, String> header;
	private StringBuilder data;
	private enum encodingsAvailable {gzip, entity};
	private encodingsAvailable contentEncoding;
	private byte[] dataToBeSent;
	private HttpStates httpStates = new HttpStates();
	private String httpState = httpStates.getState(200);
	private Map<String, String> requestHeader;

	public HTTPResponse(OutputStream output, Map<String, String> requestHeader) {
		this.output = output;
		this.requestHeader = requestHeader;
		
		data = new StringBuilder();
		
		header = new HashMap<String, String>();
		
	}
	
	public void setHeader(String key, String value){
		header.put(key, value);
	}
	
	public void removeHeader(String key){
		header.remove(key);
	}
	
	public void write(String data){
		this.data.append(data);
	}
	
	public void sendResponse() throws IOException{
		encodeData();
	
		populateHeader();
		
		output.write(("HTTP/1.0 "+httpState).getBytes("utf-8"));
		output.write("\r\n".getBytes("utf-8"));
		for(Entry<String, String> headerEntry: header.entrySet()){
			output.write((headerEntry.getKey()+": "+headerEntry.getValue()).getBytes("utf-8"));
			output.write("\r\n".getBytes("utf-8"));
		}
		output.write("\r\n".getBytes("utf-8"));
		output.write(dataToBeSent);
		
		output.flush();
	}
	
	private void encodeData() {
		String requestedEncoding = requestHeader.get("Accept-Encoding");
		if(requestedEncoding == null){
			contentEncoding = encodingsAvailable.entity;
		}else{
			String[] requestedEncodingSplit = requestedEncoding.split(" *, *");
			
			for(String encoding :requestedEncodingSplit){
				try{
					contentEncoding = encodingsAvailable.valueOf(encoding);
					break;
				}catch(IllegalArgumentException e){	
				}
			}
		}
		
		switch (contentEncoding){
		case entity: dataToBeSent = data.toString().getBytes(); break;
		case gzip: dataToBeSent = gzip(data.toString()); break;
		}
		
	}

	private void populateHeader() {
		String acceptType = requestHeader.get("Accept");
		if(acceptType!=null){
			header.put("Content-Type", acceptType.split(",")[0]);
		}
		
		header.put("Connection", "close");
		header.put("Content-Length", ""+(dataToBeSent.length));
		header.put("Status", httpState);
		header.put("Date", HTTPServer.getDateRFC1123());
		header.put("Server", HTTPServer.getPreference("server.name", "Kawa"));
		header.put("Content-Encoding", contentEncoding.name());
	}

	public void closeStream() throws IOException{
		output.close();
	}
	
	public static byte[] gzip(String str){
        if (str == null || str.length() == 0) {
            return str.getBytes();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes("utf-8"));
	        gzip.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return out.toByteArray();
     }

	public void setHTTPCode(int code){
		this.httpState = httpStates.getState(code);
	}
}
