package com.eastnets.service.dataAnalyzer;

import java.util.Map;

public class UrlServiceBuilder { 


	public  String buildRestApiUrl(String baseUrl,String restServiceType,Map<String,String> paramMap){
		StringBuilder builder=new StringBuilder(baseUrl);	
		builder.append(restServiceType);
		if(paramMap == null) {
			return builder.toString();
		}
		builder.append("?");

		int count=0;
		int size=paramMap.size();
		for(String key:paramMap.keySet()){
			String value=paramMap.get(key);
			builder.append(key+"="+value);
			count++;
			if(count != size) builder.append("&");
		}
		return builder.toString();

	} 





}
