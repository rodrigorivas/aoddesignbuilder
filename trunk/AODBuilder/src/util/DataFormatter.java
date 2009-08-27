package util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.sql.rowset.serial.SerialBlob;

public class DataFormatter  {
 
	private static final String BIG_DECIMAL_TYPE = "BigDecimal";
	public static final String INT_FIELD = "int";
	public static final String PRIMITIVE_FLOAT_FIELD = "float";
	public static final String DATE_FIELD = "Date";
	public static final String JAVA_SQL_TIMESTAMP_FIELD = "java.sql.Timestamp";
	public static final String JAVA_SQL_DATE_FIELD = "java.sql.Date";
	public static final String JAVA_UTIL_DATE_FIELD = "java.util.Date";
	public static final String FLOAT_FIELD = "Float";
	public static final String INTEGER_FIELD = "Integer";
	public static final String STRING_FIELD = "String";
	public static final String BOOLEAN_FIELD = "boolean";
	
	public static String BEAN_CONVERTED_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static String SHORT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static String safeTrim(String str) {
		 if(str == null)
		 	return "";
		 return str.trim();
	}
	
	
	public static int strLength(String str) {
		
		int len = 0;
		if ( str != null) {
			str = str.trim();
			len = str.length();
		}
		else {
			len = 0;
		}
		
		return len;
	}
	
	public static String formatDouble(Double number) {
		
		String str = "";
		 
		try {
			if ( number != null) {
				NumberFormat formatter = new DecimalFormat("0.00");
				str = formatter.format(number);  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return str;
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
	
	public static double parseDouble(String numberString, double defaultValue) {
		
		double number = defaultValue;
		 
		try {
			
			if(numberString != null &&  numberString.trim().length() > 0){
 				number = Double.parseDouble(numberString);
			}
 		 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return number;
	}

	public static double parseDouble(String numberString) {
		return parseDouble(numberString, 0.00);
	}
	
	public static int parseInt(String numberString, int defaultValue) {
		
		int number = defaultValue;
		 
		try {
			
			if(numberString != null &&  numberString.trim().length() > 0){
				number = Integer.parseInt(numberString);
			}
 		 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return number;
	}
	
	public static int parseInt(String numberString) {
		return parseInt(numberString, 0);
	}
	
	public static String integerToString(int number) {
		
		String str = "0";
		
		try {
			str = Integer.toString(number);
		} catch (Exception e) {
 			e.printStackTrace();
		}
		 
		return str;
	}
	
	public static java.util.Date parseJavaDate(java.sql.Date date) {

		java.util.Date javaDate = null;
		try {
			if (date != null) {
				javaDate = new java.util.Date(date.getTime());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return javaDate;
	}


	public static Blob stringToBlob(String string) {
		
		Blob blob = null;
		
		byte[] bytes = string.getBytes();
		
		try {
			blob = new SerialBlob(bytes);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blob;
				
	}
	
	public static Blob outputStreamToBlob (ByteArrayOutputStream out){
		Blob blob = null;
		
		byte[] bytes = out.toByteArray();
		
		try {
			blob = new SerialBlob(bytes);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blob;
				
	}
	
	public static Blob bytesToBlob (byte[] bytes){
		Blob blob = null;
				
		try {
			blob = new SerialBlob(bytes);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blob;
				
	}
	
	public static void blobToOutputStream (Blob blob, OutputStream out){
		byte[] bytes=null;
		
		try {
			bytes = blob.getBytes(1, (int) blob.length());
			out.write(bytes);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	public static Blob fileToBlob(File file) {

		Blob blob = null;
		
		try {
			//Create the Stream
			InputStream is = new FileInputStream(file);
			long length = file.length();

			// Array of bytes where the file will be stored
			byte[] bytes = new byte[(int)length];

			// Read the file bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			// Check if all the bytes had been readed
			if (offset < bytes.length) {
				throw new IOException("The file couldn't be read completely: "+file.getName());
			}

			is.close();
		
			blob = new SerialBlob(bytes);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blob;
				
	}

	public static boolean compareBlob(Blob blob1,Blob blob2){
		boolean equals = false;
		
		try {
			if ((int) blob1.length() != blob2.length()) {
				return false;
			} else {
				int length = (int) blob1.length();
				if (length > 0){					
					byte[] blob1bytes = blob1.getBytes(1, length);
					byte[] blob2bytes = blob2.getBytes(1, length);
					equals = Arrays.equals(blob1bytes, blob2bytes);
				}
				else
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return equals;
	}

	public static String blobToString (Blob blob){
		
		byte[] bytes=null;
		
		try {
			if (blob.length() > 0)
				bytes = blob.getBytes(1, (int) blob.length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String string = new String(bytes);
		
		return string;
	}
	
	public static byte[] blobToByteArray (Blob blob){
		
		byte[] bytes=null;
		 
			try {
				if (blob.length() > 0)
					bytes = blob.getBytes(1, (int) blob.length());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return bytes;
	}
	
	public static String getString(InputStream inputStream) throws IOException{
		int streamSize = 0;
		try {
			streamSize = inputStream.available();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		byte[] data = new byte[streamSize];
		for(int offset = 0, len = streamSize; len > 0; len -= offset){
			int bRead = inputStream.read(data,offset,len);
			len -= bRead;
			offset += bRead;
		}
		return new String(data);
	}
	
	public static boolean checkEmptyData(String data) {		
		data = data.replaceAll("\"\"", "");
		data = data.replaceAll(",", "");		
		return (data.trim().equals(""));
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


	@SuppressWarnings("deprecation")
	public static boolean isOlderThan(Date date, int days) {
		if (date != null){
			java.util.Date tempDate = new Date(date.getTime());
			tempDate.setHours(0);
			tempDate.setMinutes(0);
			tempDate.setSeconds(0);
			java.util.Date newDate = new Date();
			newDate.setHours(0);
			newDate.setMinutes(0);
			newDate.setSeconds(0);			
			long time = newDate.getTime() - days*24*60*60*1000; //days * 24hours * 60minutes * 60seconds * 1000miliseconds
			newDate.setTime(time);
			if(tempDate.before(newDate)) {
				return true;
			}
		}
		return false;
	}

	public static String convertIllegalChars(String word){
		if (word == null)
			return "";
		
		StringBuffer ret = new StringBuffer(word.length());
		
		for (char c: word.toCharArray()){
			switch (c) {
			case 'á':
				c = 'a';
				break;
			case 'é':
				c = 'e';
				break;
			case 'í':
				c = 'i';
				break;
			case 'ó':
				c = 'o';
				break;
			case 'ú':
				c = 'u';
				break;
			case 'ü':
				c = 'u';
				break;
			case 'ñ':
				c = 'n';
				break;
			default:
				break;
			}
			
			ret.append(c);
		}
		
		return ret.toString().trim();
	}
	
	public static String clean(String sentence){
		String firstPass = convertIllegalChars(sentence);
		String secondPass = removePunctuationMarks(firstPass);
		
		return secondPass;
	}


	private static String removePunctuationMarks(String sentence) {
		if (sentence == null)
			return "";
		
		StringBuffer ret = new StringBuffer(sentence.length());
		
		for (char c: sentence.toCharArray()){
			if (c==',' || c=='.' || c==';' || c=='\n' || c=='\t')
				c = ' ';
			
			ret.append(c);
		}
		
		return ret.toString().trim();
	}
}
