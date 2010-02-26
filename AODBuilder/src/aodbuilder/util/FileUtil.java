package aodbuilder.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {


	/** @param filePath the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be better, and buffer sizes are hardcoded
	 */
	public static String readFileAsString(String filePath)
			throws java.io.IOException {
		String data = null;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			byte[] byteData = new byte[inputStream.available()];
			inputStream.read(byteData);
			data = new String(byteData);
			inputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + filePath + " not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception.");
			e.printStackTrace();
		} finally{
			if (inputStream != null)
				inputStream.close();				
		}

		return data;
	}

	/** @param filePath the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be better, and buffer sizes are hardcoded
	 */
	public static byte[] readFileAsByte(String filePath)
			throws java.io.IOException {
		byte[] byteData = null;
		FileInputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream(filePath);
			byteData = new byte[inputStream.available()];
			inputStream.read(byteData);

		} catch (FileNotFoundException e) {
			System.out.println("File " + filePath + " not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception.");
			e.printStackTrace();
		} finally{
			if (inputStream != null)
				inputStream.close();				
		}
		return byteData;
	}

	/** @param filePath the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be better, and buffer sizes are hardcoded
	 */
	public static String writeFileFromString(String output, String filePath)
			throws java.io.IOException {
		String data = null;
		OutputStream out = null;
		try {
			if (output!=null && output.length()>0){
				out = new FileOutputStream(filePath);
				out = new BufferedOutputStream(out);
	
				out.write(output.getBytes());
	
			}

		} catch (FileNotFoundException e) {
			System.out.println("File " + filePath + " not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception.");
			e.printStackTrace();
		} finally{
			if (out != null)
				out.close();
		}
		return data;
	}
	
	public static void writeFileFromByte(byte[] content, String outputFileName) {
		File outFile = null;
		FileOutputStream fileStream = null;
		if (!outputFileName.equals("") && content != null && content.length > 0) {
			try {
				outFile = new File(outputFileName);
				fileStream = new FileOutputStream(outFile);
				if (fileStream != null)
					fileStream.write(content);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					if (fileStream != null)
						fileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
