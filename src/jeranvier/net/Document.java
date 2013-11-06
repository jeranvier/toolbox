package jeranvier.net;

public class Document{
	
	private String content;

	public Document(String content){
		this.content=content;
	}
	
	public static Document parseDocument(String document){
		return new Document(document);
	}

	public String getContent() {
		return content;
	}

}
