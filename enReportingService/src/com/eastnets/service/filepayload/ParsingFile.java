package com.eastnets.service.filepayload;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.eastnets.domain.filepayload.FilePayload;

public abstract class ParsingFile {
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	public abstract boolean parsingFile(File xmlFile, List <String> sepaXSD, FilePayload payload, List <String> filesEXT);
	

	protected static String getDateStr() {
		return dateFormat.format(new Date());
	}
	
	public boolean isFileCompressed (File xmlFile, FilePayload payload){

		try{
			//to check the file is compressed or not
			RandomAccessFile randomFile = new RandomAccessFile(xmlFile, "r");
			long num = randomFile.readInt();
			randomFile.close();
			if (num == 0x504B0304){
				System.out.println(getDateStr() +"Processing a compresessd file For <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
				return true;}
			else{

				System.out.println(getDateStr() + "Processing an un-compresessd file For <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
				return false;}	
		} catch (Exception e) {
			System.out.println(getDateStr() +e.getMessage()+ " <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
			return false;
		}
	}
}
