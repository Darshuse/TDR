package com.eastnets.test.reporting.testcase;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.service.reporting.ReportingService;
import com.eastnets.test.BaseTest;

public class reportSchedule extends  BaseTest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6451238014023140005L;

	@Test
	public void getSchedule(){
		
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		
		List<GeneratedReport> reps= reportingService.getGeneratedReports((long)1);
		for(GeneratedReport rep:reps){
			System.out.println(rep);
		}
	}

	@Test
	public void writeGeneratedReport() {
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		ReportSet rs=new ReportSet();
		List<GeneratedReport> reps = reportingService.getGeneratedReports((long) 1);
		for (GeneratedReport rep : reps) {
			rs.setId(rep.getCriteriaId());
			ReportSet reportSet=reportingService.getReportSet("", rs);

			InputStream inputStream=reportingService.getGeneratedReport(rep.getId());			
			saveToFile("c:/tmp/",rep.getEndTime(),reportSet.getName(),rep.getFormat(),inputStream);
		}
	}
	
	private String saveToFile(String directoryName , Date date,String citeria,String extension, InputStream exportReport){
		String dateFormated = "ddMMyyyy_HHmm_ssSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormated);
		String fileName = directoryName+"/"+citeria+"_"+simpleDateFormat.format(date)+"."+extension;
		FileOutputStream fileOutputStream = null;
		try {
				File file = new File(fileName);
				byte [] buf= new byte[exportReport.available()];
				fileOutputStream = new FileOutputStream(file);
				while(exportReport.read(buf)>0)
					fileOutputStream.write(buf);

				
			} catch (Exception e) {
				e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null)
					fileOutputStream.close();
				if(exportReport!=null)
					exportReport.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}// finally
		

		return null;
	}
}
