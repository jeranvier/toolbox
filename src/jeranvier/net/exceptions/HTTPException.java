package jeranvier.net.exceptions;

public class HTTPException extends Exception{
	private static final long serialVersionUID = 7840891622267820055L;

	public HTTPException(String message){
		super(message);
	}
	
	public HTTPException(){
		super();
	}
}
