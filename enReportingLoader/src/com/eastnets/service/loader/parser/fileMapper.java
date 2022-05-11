package com.eastnets.service.loader.parser;

import java.util.Date;

import org.apache.log4j.Logger;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.Rfile;
import com.eastnets.domain.loader.RfilePK;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.Message;

/**
 * @author MKassab
 * 
 */
public class fileMapper extends MessageMapper {

	private static final Logger LOGGER = Logger.getLogger(fileMapper.class);

	public static Rfile mappFile(DataPDU pdu) throws Exception {
		Rfile rfile = new Rfile();
		RfilePK rfilePK = new RfilePK();
		rfile.setId(rfilePK);
		Message message = getMessageNode(pdu);
		rfile.setMesgFileLogicalName(message.getFileLogicalName());
		rfile.setMesgFileDigestAlgo(message.getSecurityInfo().getSWIFTNetSecurityInfo().getFileDigestAlgorithm().value());
		rfile.setMesgFileDigestValue(message.getSecurityInfo().getSWIFTNetSecurityInfo().getFileDigestValue());
		rfile.setLastTry(new Date());
		rfile.setTrialsCount(0);
		rfile.setRequested(0);
		return rfile;

	}

	public static Rfile mappFile(AMP amp, LoaderMessage loaderMessage) throws Exception {
		Rfile rfile = new Rfile();
		RfilePK rfilePK = new RfilePK();
		rfile.setId(rfilePK);
		Message message = getMessageNode(amp);
		rfile.setMesgFileLogicalName(message.getFileLogicalName());
		try {
			rfile.setMesgFileDigestAlgo(message.getSecurityInfo().getSWIFTNetSecurityInfo().getFileDigestAlgorithm().value());
			rfile.setMesgFileDigestValue(message.getSecurityInfo().getSWIFTNetSecurityInfo().getFileDigestValue());
		} catch (Exception e) {
			LOGGER.error("");
		}
		rfile.setLastTry(new Date());
		rfile.setTrialsCount(0);
		rfile.setRequested(0);
		rfile.setPayloadTest(loaderMessage.getPayload());
		try {

			rfile.setMesgFileSize(Long.parseLong(amp.getData().getSize()));
		} catch (Exception e) {
			rfile.setMesgFileSize(0);

		}
		return rfile;

	}

}
