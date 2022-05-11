package com.eastnets.enGpiLoader.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.utility.GpiDirectory;

public class GpiService {

	private static final Logger LOGGER = Logger.getLogger(GpiService.class); 
	public static  List<NotifierMessage>  compareGpiMessages(List<NotifierMessage>  entities,Map<String, GpiDirectory> gpiDirectoryMap,String duration) throws ParseException{
		List<NotifierMessage>  resultEntities=new ArrayList<NotifierMessage>(); 
		String cutOfTime="";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String curuntDate = "";

		for(NotifierMessage entity:entities){  
			/*	Date date = new Date();
			SimpleDateFormat parserUTC = new SimpleDateFormat("HH:mm");
			parserUTC.setTimeZone(TimeZone.getTimeZone("UTC")); 
			String utcTime=parserUTC.format(date);
			SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
			Date utcDate = parser.parse(utcTime); 
			GpiDirectory gpiDirectory=gpiDirectoryMap.get(entity.getMesgSenderX1()+entity.getxFinCcy());
			if(gpiDirectory !=null){
				cutOfTime=gpiDirectory.getCutOfTime().substring(0,5);
				Date cutdate = parser.parse(cutOfTime); 
				try { 
					if (utcDate.after(cutdate)) {
						resultEntities.add(entity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				resultEntities.add(entity);
			} 
			cutOfTime="";*/ 
			curuntDate = formater.format(new Date());
			if(entity.getDataSource().equals(DataSource.SAA)){
			
				if(isMsgExceededDuration(entity.getMesgCreaDateTimeStr(),curuntDate,duration)){
					resultEntities.add(entity);
				} 	
			}else if(entity.getDataSource().equals(DataSource.MQ)){
				if(isMsgExceededDuration(entity.getInsertionMessageDataTime(),curuntDate,duration)){
					resultEntities.add(entity);
				}
			}
			
				

		}
		return resultEntities;

	}


	public static boolean isMsgExceededDuration(String creatDate, String curuntDate,String duration) {
		if(creatDate.isEmpty())
			return true;
		String dateStart = creatDate;
		String dateStop = curuntDate;

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null; 
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		long diffSeconds = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			diffDays = diff / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if(!duration.contains(":")){
			duration=duration+":";
		} 

		String []durArr=duration.split(":"); 
		if(durArr[0].equalsIgnoreCase("00") && durArr[1].equalsIgnoreCase("00")){
			return true;	
		}
		
		if(diffDays >= 1){
			return true;
		}else{

			
			if(!durArr[0].equalsIgnoreCase("00")){
				if(diffHours > Long.parseLong(durArr[0])){ 
					return true;
				}else if(diffHours == Long.parseLong(durArr[0])) {
					if(durArr[1].equalsIgnoreCase("00")){
						return true; 
					}else{
						if(diffMinutes >= Long.parseLong(durArr[1]) ){ 
							return true;
						}
						else {
							return false;
						}
					}
				}else if(diffHours < Long.parseLong(durArr[0])){
					return false;
				} 
			}
			
			else {
				if( diffHours == 0){
					if(!durArr[1].equalsIgnoreCase("00")){
						if(diffMinutes >= Long.parseLong(durArr[1])){ 
							return true;
						} 
					}
				}else if(!durArr[1].equalsIgnoreCase("00")){
					return true;

				}

			}
			
		}
		return false;

	}


}
