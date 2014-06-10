package jeranvier.io;

public class UnknownFieldException extends Exception {

	private static final long serialVersionUID = 8588689747104341873L;

	public UnknownFieldException(){
		super("No field with this name");
	}
}
