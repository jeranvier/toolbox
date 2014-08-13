package jeranvier.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ArrayManipulator {
	
	public static <T> T[] extractField(Object[] data, Field field){
		List<T> fieldArray = new ArrayList<T>();
		for(Object object : data){
			fieldArray.add(object.getClass().getField(name))
		}
		return (T[]) fieldArray.toArray();
	}

}
