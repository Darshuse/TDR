package com.eastnets.dbconsistencycheck.loadervalidators;

import org.apache.log4j.Logger;
import com.eastnets.dbconsistencycheck.app.DBConsistencyCheckConfig;

public class CommandParamValidator {

	Logger log = Logger.getLogger("DBConsistencyCheck");

	public String checkDBConnection(DBConsistencyCheckConfig cnfgBean) {

		log.info("Check database connection parameters");

		if (cnfgBean.getServerName() == null || cnfgBean.getServerName().length() <= 0){
			log.error("one or more DB parameters were not set");
			return "-ip";
		}
		if (cnfgBean.getUsername() == null 	|| cnfgBean.getUsername().length() <= 0){
			log.error("one or more DB parameters were not set");
			return "-u";
		}

		if (cnfgBean.getPassword() == null || cnfgBean.getPassword().length() <= 0){
			log.error("one or more DB parameters were not set");
			return "-p";
		}

		if (cnfgBean.getPortNumber() == null || cnfgBean.getPortNumber().length() <= 0){
			log.error("one or more DB parameters were not set");
			return "-port";
		}

		if (cnfgBean.getDatabaseName() == null || cnfgBean.getDatabaseName().length() <= 0){
			if(cnfgBean.getDbServiceName()== null ||cnfgBean.getDbServiceName().length() == 0){
				log.error("one or more DB Parameters were not set");
				return "-dbname or -servicename";
			}
		}
		return "VALID";
	}

	public String checkMandatoryParam(DBConsistencyCheckConfig cnfgBean) {

		log.info("check mandatory paramaters");

		if (cnfgBean.getSAAServer() == null || cnfgBean.getSAAServer().length() <= 0){
			log.error("one or more mandatory parameters were not set");
			return "-SAA_IP";
		}
		if (cnfgBean.getSAAPort() == 0 || cnfgBean.getSAAPort()  <= 0){
			log.error("one or more mandatory parameters were not set");
			return "-SAXS_PORT";
		}
		if (cnfgBean.getReportPath() == null || cnfgBean.getReportPath().length() <= 0){
			log.error("one or more mandatory parameters were not set");
			return "-RptFile";
		}
		if ( cnfgBean.getFrequency() <= 0){
			log.error("one or more mandatory parameters were not set");
			return "-frequency";
		}
		if (cnfgBean.getDayNum() == 0 || cnfgBean.getDayNum() <= 0){
			log.error("one or more mandatory parameters were not set");
			return "-d";
		}
		/*if (cnfgBean.getActiveTime() == null){
			log.error("one or more mandatory parameters were not set");
			return "-ActiveTime";
		}*/	
		if (cnfgBean.getSleepPeriod() <= 0){
			log.error("one or more mandatory parameters were not set");
			return "-SleepPeriod";
		}
		
		if (cnfgBean.getSleepTime() == null){
			log.error("one or more mandatory parameters were not set");
			return "-SleepTime";
		}
		if (cnfgBean.getAid()  < 0){
			log.error("one or more mandatory parameters were not set");
			return "-I";
		}
		return "VALID";
	}

	public String checkEmailMandatoryParams(DBConsistencyCheckConfig cnfgBean) {
		log.debug("Check email mandatory paramters");
		
		if (cnfgBean.getMailHost() == null
				|| cnfgBean.getMailHost().length() <= 0)
			return "-mail";
		if (cnfgBean.getMailPort() == null
				|| cnfgBean.getMailPort().length() <= 0)
			return "-mailPort";
		if (cnfgBean.getMailFrom() == null
				|| cnfgBean.getMailFrom().length() <= 0)
			return "-mailfrom";
		if (cnfgBean.getMailTo() == null
				|| cnfgBean.getMailTo().length() <= 0)
			return "-mailto";

		return "VALID";
	}
}
