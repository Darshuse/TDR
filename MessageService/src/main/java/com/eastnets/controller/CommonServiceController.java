package com.eastnets.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eastnets.entities.AppendixPK;
import com.eastnets.response.dto.AppendixDTO;
import com.eastnets.service.CommonService;


@RestController
@RequestMapping("v1/common")
public class CommonServiceController {

	@Autowired
	CommonService commonService;

	private SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("/appendixInfo")
	public AppendixDTO getAppendixInfo(@RequestParam(value = "aid") Long aid, @RequestParam(value = "umidl") Long umidl,
			@RequestParam(value = "umidh") Long umidh, @RequestParam(value = "instNumber") Long instNum,
			@RequestParam(value = "appeDateTime") String appeDateTime,
			@RequestParam(value = "seqNumber") Long seqNumber) {

		try {

			List<AppendixDTO> appendixInfo = commonService.getAppendixInfo(
					new AppendixPK(aid, umidh, umidl, instNum, fullDateTimeFormat.parse(appeDateTime), seqNumber));

			if (appendixInfo != null && !appendixInfo.isEmpty()) {
				return appendixInfo.get(0);
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
