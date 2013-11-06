package jeranvier.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriter {

	private boolean opened;
	private String filePath;
	private PrintWriter printWriter;

	public FileWriter(String filePath){
		this.filePath = filePath;
		this.opened = false;
		this.printWriter = null;
		
		File file = new File(filePath);
		 
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Could not create the file: "+filePath);
				e.printStackTrace();
			}
		}
	}
	
	public void append(Object object) throws FileNotFoundException{
		if(!opened){
			printWriter = new PrintWriter(filePath);
			opened = true;
		}
		
		printWriter.print(object.toString());
	}
	
	public void appendln(Object object) throws FileNotFoundException{
		if(!opened){
			printWriter = new PrintWriter(filePath);
			opened = true;
		}
		
		printWriter.println(object.toString());
	}
	
	public void clear() throws IOException{
		File file = new File(filePath);
		if(file.delete()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Could not create the file: "+filePath);
				e.printStackTrace();
			}
		}
		else{
			throw new IOException("Could not create the file: "+filePath);
		}
	}
	
	public void close(){
		if(opened){
			printWriter.close();
			opened = false;
		}
	}
}
