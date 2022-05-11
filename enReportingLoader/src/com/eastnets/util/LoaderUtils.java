package com.eastnets.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Clob;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * General utilities for Loader Project
 * @author OAlKhalili
 *
 */
public class LoaderUtils {

	private static final Logger LOGGER = Logger.getLogger(LoaderUtils.class);
	
	/**
	 * Convert passed Clob to String
	 * @param clob
	 * @return
	 */
	public static String convertClob2String(Clob clob) {
		String clobAsString = "";
		if (clob == null)
			return clobAsString;
		InputStream in;
		try {
			in = clob.getAsciiStream();
			StringWriter w = new StringWriter();
			IOUtils.copy(in, w);
			clobAsString = w.toString();
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		}
		return clobAsString;
	}
	
	
	
	
	
	
	
	
	
}
