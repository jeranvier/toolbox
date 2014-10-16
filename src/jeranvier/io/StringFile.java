package jeranvier.io;


public class StringFile {
	private String string;

	public StringFile(String string) {
		this.string = string;
	}
	
	public String getString(){
		return this.string;
	}

	
	public void setString(String string) {
		this.string = string;
	}

	public static StringFile parseStringFile(String string) throws Exception{
		return new StringFile(string);
	}
	
	public String toString(){
		return string;
	}

}
