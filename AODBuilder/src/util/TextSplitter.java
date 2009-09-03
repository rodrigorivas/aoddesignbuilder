package util;

import java.util.regex.Pattern;

public class TextSplitter {

	public static String COMMON_PATTERN = "[';.,\\s]+";

	public static String [] split (String text) {
		Pattern splitter = Pattern.compile(COMMON_PATTERN);
		String [] result = splitter.split(text);
		return result;
	}

	public static String [] split (String text, String pattern) {
		Pattern splitter = Pattern.compile(pattern);
		String [] result = splitter.split(text);
		return result;
	}

}
