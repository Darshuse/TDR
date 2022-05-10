package com.eastnets.enGpiNotifer.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.FileUtil;

import com.eastnets.service.viewer.ViewerServiceImp;
import com.mifmif.common.regex.Generex;


public class GpiHelper {

	private static final Logger LOGGER = Logger.getLogger(ViewerServiceImp.class);
	public static void writeConfirmation(String confirmations,String filePath    ){

		FileWriter fw = null;
		BufferedWriter bw = null;
		String tempFilePath="";
		try { 
			makeDir(filePath);
			tempFilePath=filePath+"/temp/Confirmation"+new Date().getTime()+".txt";
			fw = new FileWriter(tempFilePath);
			bw = new BufferedWriter(fw);
			bw.write(confirmations);  

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) { 
				LOGGER.info("filePathDirectory :  Does not exist ");
				ex.printStackTrace();

			}
		}

	}



	private static void makeDir(String dir){
		File file = new File(dir+"/temp");
		file.mkdir(); 

	} 
	public static void moveBack(String from,String to) throws IOException{
		try {	
			File dir1 = new File(from);
			if(dir1.isDirectory()) {
				File[] content = dir1.listFiles();
				for(int i = 0; i < content.length; i++) {  
					content[i].renameTo(new File(to+"/"+content[i].getName()));
				}
			}
			delete(new File(from)); 

		} catch (Exception e) {
			e.printStackTrace();
		}



	}


	private static void delete(File file) throws IOException{

		if(file.isDirectory()){

			//directory is empty, then delete it
			if(file.list().length==0){
				file.delete();

			}else{

				//list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					//construct the file structure
					File fileDelete = new File(file, temp);

					//recursive delete
					delete(fileDelete);
				}

				//check the directory again, if empty then delete it
				if(file.list().length==0){
					file.delete();
					System.out.println("Directory is deleted : "+ file.getAbsolutePath());
				}
			}

		}else{
			//if file, then delete it
			file.delete();
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}


	public static   boolean renameFileExtension  (String source, String newExtension)  
	{  
		String target;  
		String currentExtension = getFileExtension(source);  

		if (currentExtension.equals("")){  
			target = source + "." + newExtension;  
		}  
		else {  
			target = source.replaceAll("." + currentExtension, newExtension);  
		}  
		return new File(source).renameTo(new File(target));  
	}  

	public static   String getFileExtension(String f) {  
		String ext = "";  
		int i = f.lastIndexOf('.');  
		if (i > 0 &&  i < f.length() - 1) {  
			ext = f.substring(i + 1).toLowerCase();  
		}  
		return ext;  
	} 
	public static void main(String[] args) {
		//
		//System.out.println("E:\\enGpiNonfier\\Confirmation1537451029845.txtTemp".replaceAll("." + "txtTemp", ".text"));
		System.out.println(new Date().getTime());
	}


	public static String generetTrnRef(String rejex){
		String trnRef="";
		if(rejex.equalsIgnoreCase("YYMMDDHHmmssfff")){
			trnRef=new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		}else{
			String rejexFormateed=rejex.replaceAll("/", "//");
			Generex generex = new Generex(rejexFormateed); 
			trnRef = generex.random();
		}
		return trnRef;

	}


}
