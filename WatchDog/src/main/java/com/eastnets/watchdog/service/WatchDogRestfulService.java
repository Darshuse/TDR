package com.eastnets.watchdog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eastnets.entities.AppendixPK;
import com.eastnets.watchdog.checker.AppendixDTO;

@Service
public class WatchDogRestfulService {

	private RestTemplate restTemplate = new RestTemplate();

	public AppendixDTO getAppendixInfo(AppendixPK id) {
		AppendixDTO appendixDTO = restTemplate.getForObject(
				"http://localhost:8080/v1/common/appendixInfo?umidl=620077265&umidh=-490&instNumber=0&appeDateTime=2018-05-27 10:28:47&seqNumber=14705&aid=1",
				AppendixDTO.class);
		return appendixDTO;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
