package aodbuilder.util;

public class ExceptionUtil {

	public static String getTrace(Exception e){
		String message = "";
		if (e!=null){
			message = e.getMessage()+"\n";
			for (StackTraceElement ste: e.getStackTrace()){
				message+=ste.toString()+"\n";
			}
		}
		return message;
	}
}
