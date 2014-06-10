package jeranvier.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
	
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
		
		public int getInt(String fieldName) throws UnknownFieldException{
			return getInt(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private int getInt(int index) {
			return Integer.parseInt(values[index]);
		}
		
		public long getLong(String fieldName) throws UnknownFieldException{
			return getLong(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private long getLong(int index) {
			return Long.parseLong(values[index]);
		}
		
		public double getDouble(String fieldName) throws UnknownFieldException{
			return getDouble(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private double getDouble(int index) {
			return Double.parseDouble(values[index]);
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

		private char getChar(int index) {
			return values[index].charAt(0);
		}
		
		public boolean getBoolean(String fieldName) throws UnknownFieldException{
			return getBoolean(CSVHandler.this.header.getFieldIndex(fieldName));
		}

		private boolean getBoolean(int index) {
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

	
	public static void main(String[] args) throws IOException, UnknownFieldException{
		CSVHandler csv = new CSVHandler(',', '"', true);
		csv.loadFile("F:/desktop/export_memorySense.csv");
		System.out.println(csv.getHeader().toString());
		System.out.println(csv.getRecord(1));
		System.out.println(csv.getRecord(1).getLong("event_end_time"));
		System.out.println(csv.getRecord(1).getLong(1));
	}

}
