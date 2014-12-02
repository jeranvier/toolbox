package jeranvier.io;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class FileLoader<T> {
	
	private final Class<T> classType;
	private String filePath;
	private Method parsingMethod;


	public FileLoader(String filePath, Class<T> type){
		this.filePath = filePath;
		this.classType = type;
		try {
			this.parsingMethod = findParsingMethod();
		} catch (NoSuchMethodException e) {
			System.err.println("Impossible to find a valid parsing method (static method taking a string in argument and returning an instance of the class) in class: "+type.getCanonicalName());
			e.printStackTrace();
		}
	}
	
	
	private Method findParsingMethod() throws NoSuchMethodException {
		for(Method method : classType.getMethods()){
			if(method.getName().equals("parse"+classType.getSimpleName())
					&& Modifier.isStatic( method.getModifiers())
					&& method.getReturnType().equals(classType)
					&& method.getParameterTypes().length ==1){
				return method;
			}
		}
		throw new NoSuchMethodException();
	}


	/**
	 * Load a file from disk and load it into a list of type T
	 * @throws IOException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<T> loadFileInList() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<T> list= new ArrayList<T>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = br.readLine()) != null) {
				list.add((T) parsingMethod.invoke(null, line));
			}
			br.close();
		}catch(FileNotFoundException e){
			System.out.println("File not found:" +filePath);
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public T loadFileInObject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		
		StringBuilder fileBuilder = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		
		while ((line = br.readLine()) != null) {
			fileBuilder.append(System.lineSeparator());
			fileBuilder.append(line);
		}
		
		br.close();

		
		return (T) parsingMethod.invoke(null, fileBuilder.toString().substring(2));
	}

}
