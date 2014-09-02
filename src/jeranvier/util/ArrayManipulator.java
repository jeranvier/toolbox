package jeranvier.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayManipulator {
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> extractField(Object[] data, Method method) {
		List<T> fieldList = new ArrayList<T>();
		
		if (data.length<1){
			return fieldList;
		}
		
		for(Object object : data){	
			try {
				fieldList.add((T)method.invoke(object));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fieldList;
		
	}

	public static <T> List<T> extractField(List<? extends Object> data, Method method) {
		return extractField(data.toArray(), method);
	}
	
}


