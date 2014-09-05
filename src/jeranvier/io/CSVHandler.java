package jeranvier.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
	
	public static final char TAB = '\t';
	public static final char EMPTY = '\u0000';
	private char separator;
	private char quote;
	private boolean headerAvailable;
	private List<Record> records;
	private Header header;
	private int numberOfFields;
	
	public CSVHandler(char separator, char quote, boolean headerAvailable){
		this.separator = separator;
		this.quote = quote;
		this.headerAvailable = headerAvailable;
		this.records = new ArrayList<Record>();
	}
	
	public void loadFile(String filePath) throws IOException{
		try{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			if(headerAvailable){
				line = br.readLine();
				header = parseHeader(line);
				numberOfFields = header.getNumberOfFields();
			}
			while ((line = br.readLine()) != null) {
				records.add(parseRecord(line));
			}
			br.close();
		}catch(FileNotFoundException e){
			System.out.println("File not found:" +filePath);
		}
	}
	
	public Record parseRecord(String line){
		String[] values = new String[numberOfFields];
		parseFields(line).toArray(values);
		return new Record(values);
	}
	
	public Header parseHeader(String line){
		return new Header(parseFields(line));
	}
	
	public List<String> parseFields(String line){
		List<String> fields = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean evenQuote = true;
		for(int i = 0; i<line.length(); i++){
			//if <separator> after an even # of <quote> we are done with the current value
			if(line.charAt(i) == separator && evenQuote){
				fields.add(decode(sb.toString()));
				sb = new StringBuilder();
			}
			else{
				//keep track of the number of quotes (odd = in a field, even = between two fields)
				if(line.charAt(i) == quote){
					evenQuote = !evenQuote;
				}
				sb.append(line.charAt(i));
			}
		}
		fields.add(decode(sb.toString()));
		return fields;
	}
	
	private static String decode(String value) {
		value = value.replaceAll("^\\ *\"(.*)\"\\ *$","$1");
		value = value.replaceAll("\"{2}","\"");
		return value;
	}
	
	private static String encode(String value) {
		value = value.replaceAll("^\\ *\"(.*)\"\\ *$","$1");
		value = value.replaceAll("\"{2}","\"");
		return value;
	}

	public List<Record> getRecords(){
		return this.records;
	}
	
	public Header getHeader(){
		return this.header;
	}
	
	public Record getRecord(int i){
		return this.records.get(i);
	}
	
	
	public class Record{
		
		private String[] values;
		
		public Record(String[] values){
			this.values = values;
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<values.length; i++){
				sb.append(values[i]);
				if(i != values.length-1){
					sb.append(CSVHandler.this.separator);
				}
			}
			return sb.toString();
		}
		
		public Integer getInt(String fieldName) throws UnknownFieldException{
			return getInt(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private Integer getInt(int index) {
			String string = values[index];
			if(string.length() == 0)
				return null;
			else
				return Integer.parseInt(string);
		}
		
		public Long getLong(String fieldName) throws UnknownFieldException{
			return getLong(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private Long getLong(int index) {
			String string = values[index];
			if(string.length() == 0)
				return null;
			else
				return Long.parseLong(string);
		}
		
		public Double getDouble(String fieldName) throws UnknownFieldException{
			return getDouble(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private Double getDouble(int index) {
			String string = values[index];
			if(string.length() == 0)
				return null;
			else
				return Double.parseDouble(string);
		}
		
		public String getString(String fieldName) throws UnknownFieldException{
			return getString(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private String getString(int index) {
			return values[index];
		}
		
		public char getChar(String fieldName) throws UnknownFieldException{
			return getChar(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private Character getChar(int index) {
			String string = values[index];
			if(string.length() == 0)
				return null;
			else
				return values[index].charAt(0);
		}
		
		public boolean getBoolean(String fieldName) throws UnknownFieldException{
			return getBoolean(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private Boolean getBoolean(int index) {
			String string = values[index];
			if(string.length() == 0)
				return null;
			else
				return Boolean.parseBoolean(values[index]);
		}
	}
	
	public class Header{
		
		private List<String> fieldsName;
		
		public Header(List<String> fieldsName){
			this.fieldsName = fieldsName;
		}
		
		public int getNumberOfFields(){
			return fieldsName.size();
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<fieldsName.size(); i++){
				sb.append(fieldsName.get(i));
				if(i != fieldsName.size()-1){
					sb.append(CSVHandler.this.separator);
				}
			}
			return sb.toString();
		}
		
		public int getFieldIndex(String fieldName){
			return this.fieldsName.indexOf(fieldName);
		}
	}
}
