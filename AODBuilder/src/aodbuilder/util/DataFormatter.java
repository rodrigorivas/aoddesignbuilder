package aodbuilder.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataFormatter  {
 
	private static final String BIG_DECIMAL_TYPE = "BigDecimal";
	private static final String INT_FIELD = "int";
	private static final String PRIMITIVE_FLOAT_FIELD = "float";
	private static final String DATE_FIELD = "Date";
	private static final String JAVA_SQL_DATE_FIELD = "java.sql.Date";
	private static final String JAVA_UTIL_DATE_FIELD = "java.util.Date";
	private static final String INTEGER_FIELD = "Integer";
	private static final String BOOLEAN_FIELD = "boolean";
	private static final String[] returnTypes = {"int", "float", "void", "boolean", "char", "String"};
	
	private static String BEAN_CONVERTED_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
	private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static String SHORT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static String capitalize(String word) {
		if (word!=null){
			String firstLetter = Character.toString(word.charAt(0)).toUpperCase();
			word = firstLetter+word.substring(1, word.length());
		}
		return word;
	}

	
	public static String convertIllegalXMLChars(String valueStr) {
		StringBuffer value = new StringBuffer();			

		char[] buffer = valueStr.toCharArray();
		
		for (int i = 0; i < buffer.length; i++) {
			switch (buffer[i]) {
			case '&':
				value.append("&amp;");
				break;
			case '<':
				value.append("&lt;");
				break;
			case '"':
				value.append("&quot;");
				break;
			case '\'':
				value.append("&apos;");
				break;
			default:
				value.append(buffer[i]);
				break;
			}
		}
		return value.toString();
	}

	/**
	 * Converts the given String value to the corresponding value format
	 * 
	 * @param fieldType
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static Object convertValue(Class fieldType, String value, String... otherParameters ) throws ParseException {
		if (fieldType.getSimpleName().equals(INT_FIELD)){
			return Integer.parseInt(value);
		}
		if (fieldType.getSimpleName().equals(PRIMITIVE_FLOAT_FIELD)){
			return Float.parseFloat(value);
		}
		if (fieldType.getSimpleName().equals(DATE_FIELD)){
			String formatStr = null;
			if (otherParameters.length > 0){
				formatStr = otherParameters[0];
			}
			else
				formatStr = BEAN_CONVERTED_DATE_FORMAT;
			
			 DateFormat format = null;
			 Date date = null;
			 try{
				 /* try using given format */
				 format = new SimpleDateFormat(formatStr);
				 date = format.parse(value);
			 }catch(ParseException e){
				 try{
					 /* Try using the default format */
					 format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
					 date = format.parse(value);
				 }catch(ParseException e2){
					 /* Try using the short version (without time)*/
					 format = new SimpleDateFormat(SHORT_DATE_FORMAT);
					 date = format.parse(value);
				 }
			 }

			 if (fieldType.getName().equals(JAVA_UTIL_DATE_FIELD))
				 return date;
			 else if (fieldType.getName().equals(JAVA_SQL_DATE_FIELD))
				 return DataFormatter.parseSqlDate(date);
		}
		if (fieldType.getSimpleName().equals(BIG_DECIMAL_TYPE)){
			return java.math.BigDecimal.valueOf(Float.parseFloat(value));
		}
		if (fieldType.getSimpleName().equals(INTEGER_FIELD)){
			return new Integer(Integer.parseInt(value));
		}	
		if (fieldType.getSimpleName().equals(BOOLEAN_FIELD)){
			return new Boolean(Boolean.parseBoolean(value));
		}	
		return value;
	}


	public static boolean equalsRegExp(String regExp,String text) {		
		if (regExp!=null && text!=null){
			Pattern p = Pattern.compile(regExp);			
			Matcher m = p.matcher(text);
			return m.matches();
		}
		return false;
	}
	
	public static boolean equalsRegExpDual(String text1, String text2) {
		if (equalsRegExp(text1, text2))
			return true;
		return equalsRegExp(text2, text1);
	}


	public static String getSimpleFileName(String fileName) {
		fileName = removePath(fileName);
		fileName = removeExtension(fileName);
		return fileName;
	}


	public static String javanize(String text, boolean className){
		String ret = "";
		if (text!=null && !ListUtils.contains(returnTypes, text)){
			String[] words = text.split(" ");
			for (String word: words){
				if (ret.equals("") && !className){
					word = word.toLowerCase();
					ret=unCapitalize(word);
				}
				else{
					ret+=capitalize(word);
				}
			}
		}
		return Inflector.getInstance().singularize(ret);
	}

	public static java.sql.Date parseSqlDate(Date date) {
		
		java.sql.Date sqlDate = null;
		try {
			if (date != null) {
				sqlDate = new java.sql.Date(date.getTime());
			}
 	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return sqlDate;
	}
	
	public static String removeExtension(String fileName) {
		if (fileName!=null){
			int pos = fileName.indexOf(".");
			if (pos > 0){
				fileName = fileName.substring(0, pos);
			}
		}
		return fileName;
	}


	public static String removePath(String fileName) {
		if (fileName!=null){
			int pos = fileName.lastIndexOf("/");
			if (pos > 0 && pos < fileName.length()){
				fileName = fileName.substring(pos+1, fileName.length());
			}
			else{
				pos = fileName.lastIndexOf("\\");
				if (pos > 0 && pos < fileName.length()){
					fileName = fileName.substring(pos+1, fileName.length());
				}				
			}
		}
		return fileName;
	}

	public static String safeTrim(String str) {
		 if(str == null)
		 	return "";
		 return str.trim();
	}


	public static String unCapitalize(String word) {
		if (word!=null){
			String firstLetter = Character.toString(word.charAt(0)).toLowerCase();
			word = firstLetter+word.substring(1, word.length());
		}
		return word;
	}


	public static int countWords(String text) {
		int ret = 0;
		StringTokenizer token = new StringTokenizer(text);
		while (token.hasMoreTokens()){
			token.nextToken();
			ret++;
		}
		
		return ret;
	}


	public static String getFirstNWords(String text, int max) {
		String ret = "";
		int numTokens=0;
		StringTokenizer token = new StringTokenizer(text);
		while (token.hasMoreTokens() && numTokens<max){
			ret+=" "+token.nextToken();
			numTokens++;
		}
		
		return ret.trim();
	}
	

	public static String getWordsFrom(String text, int max) {
		String ret = "";
		int numTokens=0;
		StringTokenizer token = new StringTokenizer(text);
		while (token.hasMoreTokens() && numTokens<max){
			token.nextToken();
			numTokens++;
		}

		while (token.hasMoreTokens()){
			ret+=" "+token.nextToken();
		}	
		
		return ret.trim();
	}

	public static String transformInputString(String str) {
		if (str!=null){
			StringBuffer value = new StringBuffer();			
	
			char[] buffer = str.toCharArray();
			
			for (int i = 0; i < buffer.length; i++) {
				switch (buffer[i]) {
				case '\\':
					value.append(" or ");
					break;
				case '/':
					value.append(" or ");
					break;
				default:
					value.append(buffer[i]);
					break;
				}
			}
			return value.toString();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		String text = "The user enters the homepage of the site. The system asks the user to enter his username and pass-word. The system validates this information and allow the user to enter to the mainpage.";
//		System.out.println("Number of tokens: "+countWords(text));
//		System.out.println("First 5 tokens: "+ getFirstNWords(text, 5));
//		System.out.println("Last 5 tokens: "+ getWordsFrom(text, 28));
		String text = "The Student can also modify or delete course selections if changes are made within the add/drop period at the beginning of the semester.";
		System.out.println(transformInputString(text));
	}



}

