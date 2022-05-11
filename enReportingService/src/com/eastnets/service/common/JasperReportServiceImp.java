package com.eastnets.service.common;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRExpressionUtil;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.admin.User;

import com.eastnets.service.ServiceBaseImp;



/**
 * 
 * @author HAlfoqahaa
 *
 */
public abstract class JasperReportServiceImp extends ServiceBaseImp implements ReportService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4239503082926796149L;
	private DBPortabilityHandler dbPortabilityHandler;
	private DataSource dataSource;
	private final String SWAP_DIR ="swap";
	
	private CommonDAO commonDAO;
	private boolean activateProfile=true;
	protected Connection connection;
	
	protected static Logger logger=Logger.getLogger(JasperReportServiceImp.class);
	

	private void applyStyle(JasperReport jasperReport) {
		
		JRBand targetBand = jasperReport.getTitle();
		
		if(targetBand == null)
		{
			targetBand = jasperReport.getPageHeader();
		}
		
		if(targetBand != null)
		{
			JRElement[] elements  = targetBand.getElements();
			for(int i=0;i<elements.length;i++)
			{
				JRElement jrElement = elements[i];
				if(jrElement instanceof JRRectangle)
				{
					jrElement.setForecolor(Color.white);
				}
				else if(jrElement instanceof JRTextField)
				{
					jrElement.setForecolor(Color.black);
				}
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
	}

	protected ArrayList<String> getSubReportFileNames(String pm_reportPath, Collection<?> pm_expressions) 
	{
		if (pm_expressions == null || pm_expressions.isEmpty()) return null;

		ArrayList<String> names = new ArrayList<String>();
		JasperDesign design;
		try
		{
			Object[] expArray = pm_expressions.toArray();
			for(int i=0; i<expArray.length; i++)
			{
				String expr = JRExpressionUtil.getExpressionText((JRExpression)expArray[i]);
				// Search for expressions that look like : $P{SUBREPORT_DIR} + "subReport.jasper|subReport.jrxml" 
				if (expr.toUpperCase().contains("SUBREPORT_DIR") && (expr.toUpperCase().contains(".JASPER") || expr.toUpperCase().contains(".JRXML")))
				{
					// suppress undesired characters to keep only the name of the sub report 
					expr = expr.replaceAll("\\$P\\{SUBREPORT_DIR\\}", "").replaceAll("\\+", "").replaceAll("\"", "").replaceAll("(?i)\\.JASPER", "\\.jrxml").trim();

					pm_reportPath = pm_reportPath.substring(0, pm_reportPath.lastIndexOf(File.separator)+1)+expr;
					names.add(pm_reportPath); // store path/name.jrxml
					design=JRXmlLoader.load(pm_reportPath);
					JRDesignQuery newquery = new JRDesignQuery();
					newquery.setLanguage("sql");

					design.setQuery(newquery);
					JasperCompileManager.compileReport(design);
				}
			}
		}
		catch (JRException e)
		{
			e.printStackTrace();

		}
		return names;
	}
	

	/**
	 * 
	 * @param loggedInUser
	 * @param reportName
	 * @param reportExtension
	 * @param param
	 * @param excelVersion
	 * @param user
	 * preparing report parameters & determine export document 
	 * @throws Exception 
	 */
	protected void prepareForExport(JasperFillDetails jasperFillDetails,String loggedInUser,String reportName,
			 String reportExtension, Map<String, Object> param, String excelVersion,User user) throws Exception {

		connection = null;
	
			connection = dataSource.getConnection();
			if (isActivateProfile()) {
				commonDAO.activateProfile(connection, user);
			}
			JasperDesign design;
			design = JRXmlLoader.load(reportName);
			String query = design.getQuery() == null ? "" : design.getQuery().getText();

			JRDesignQuery newquery = new JRDesignQuery();
			if (query.contains("$PROC")) {
				String procName = query.substring(query.indexOf("$PROC") + 6);
				procName = procName.substring(0, procName.indexOf(")"));
				if (dbPortabilityHandler.getDbTypeString().contains("Oracle")) {
					query = "{call $PROC($P{ORACLE_REF_CURSOR},$P{@pi_operator},$P{@pi_parameters})}";
					query = query.replace("$PROC", procName);
					newquery.setText(query);
					newquery.setLanguage("plsql");
				} else {
					if (procName.indexOf(".") > 0)
						procName = procName.replace(".", ".dbo.");
					query = "exec $PROC $P{@pi_operator},$P{@pi_parameters}";
					query = query.replace("$PROC", procName);
					newquery.setText(query);
					newquery.setLanguage("sql");
				}
			} else {
				newquery.setLanguage("sql");
				newquery.setText(query);
			}

			getSubReportFileNames(reportName, design.getExpressions());
			design.setQuery(newquery);
			jasperFillDetails.setJasperReport(JasperCompileManager.compileReport(design));

			String separator = System.getProperty("file.separator");

			String swapPath = new File(reportName).getParentFile().getParent() + separator + SWAP_DIR;
			JRSwapFile file = new JRSwapFile(swapPath, 4096, 200 );
			// creating the Virtualizer
			jasperFillDetails.setVirtualizer(new JRSwapFileVirtualizer(100, file, true));
			param.put(JRParameter.REPORT_VIRTUALIZER, jasperFillDetails.getVirtualizer());

			if (jasperFillDetails.getJasperReport() != null) {
				applyStyle(jasperFillDetails.getJasperReport());
			}

	}

	/**
	 * 
	 * @param loggedInUser
	 * @param reportName
	 * @param reportExtension
	 * @param param
	 * @param excelVersion
	 * @param user
	 * @return
	 * @throws Exception
	 *             This method must be implemented by the desired classes to
	 *             determine the suitable export methodology either sync or
	 *             ASync or other possibilities.
	 */
	public abstract ByteArrayOutputStream fillReport(JasperFillDetails jasperFillDetails, String loggedInUser, String reportName, String reportExtension,
			Map<String, Object> param, String excelVersion, User user) throws Exception;

	@Override
	public ByteArrayOutputStream exportReport(JasperFillDetails jasperFillDetails, String loggedInUser, String reportName, String reportExtension, Map<String, Object> param,
			String excelVersion, User user) throws Exception {
		prepareForExport(jasperFillDetails, loggedInUser, reportName, reportExtension, param, excelVersion, user);
		return fillReport(jasperFillDetails, loggedInUser, reportName, reportExtension, param, excelVersion, user);

	}	

	@Override
	public void PrepareForNewGeneration(JasperFillDetails jasperFillDetails) {
		jasperFillDetails.getFillListener().setFillingStatus(ReportService.ReportFillingStatus.READY_FOR_NEW_REQUEST);
	}


	
	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public boolean isActivateProfile() {
		return activateProfile;
	}

	public void setActivateProfile(boolean activateProfile) {
		this.activateProfile = activateProfile;
	}


	
}

