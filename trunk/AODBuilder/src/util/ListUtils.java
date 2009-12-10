package util;

import java.util.List;

public class ListUtils {

	public static boolean contains(String[] list, String word) {
		if (list!=null){
			for (String s: list){
				if (s.equalsIgnoreCase(word))
					return true;
			}
		}
		return false;
	}
	
	public static Object get (List<?> list, Object obj){
		if (list!=null){
			for (Object o: list){
				if (o.equals(obj))
					return o;
			}
		}
		return null;
	}
}
