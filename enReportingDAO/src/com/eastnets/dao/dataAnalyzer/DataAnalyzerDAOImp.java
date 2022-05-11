package com.eastnets.dao.dataAnalyzer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.domain.dataAnalyzer.ResourceLookup;

public class DataAnalyzerDAOImp extends DAOBaseImp implements DataAnalyzerDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getBiDefaultRepository() {
		String BI_DEFAULT_PATH = jdbcTemplate.queryForObject("select BI_DEFAULT_PATH from LDGLOBALSETTINGS", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String BI_DEFAULT_PATH = rs.getString("BI_DEFAULT_PATH");
				return BI_DEFAULT_PATH;
			}
		});
		return BI_DEFAULT_PATH;
	}

	@Override
	public List<ResourceLookup> getResourcesTree(String folderName) {
		Long folderId = jdbcTemplate.queryForLong("select id from   jiresourcefolder WHERE URI LIKE '" + folderName + "'");

		if (folderId == null) {
			System.out.println("folderId is null ");
		}

		String getjIResourceFoldersQuery = "select * from jiresourcefolder resfol inner join jiresource res on resfol.id=res.CHILDRENFOLDER where resfol.parent_folder=?";
		List<ResourceLookup> jIResourceFolders = jdbcTemplate.query(getjIResourceFoldersQuery, new Object[] { folderId }, new RowMapper<ResourceLookup>() {
			public ResourceLookup mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResourceLookup resourceLookup = new ResourceLookup();
				resourceLookup.setCreationDate(rs.getDate("CREATION_DATE").toString());
				resourceLookup.setDescription(rs.getString("DESCRIPTION"));
				resourceLookup.setLabel(rs.getString("LABEL"));
				resourceLookup.setUri(rs.getString("URI").replace("_files", ""));
				resourceLookup.setResourceType(getResourceType(rs.getString("RESOURCETYPE")));
				return resourceLookup;

			}
		});
		return jIResourceFolders;
	}

	private String getResourceType(String resourceType) {
		if (resourceType == null)
			return "";
		if (resourceType.equalsIgnoreCase("com.jaspersoft.ji.adhoc.AdhocDataView")) {
			return "adhocDataView";
		} else if (resourceType.equalsIgnoreCase("com.jaspersoft.ji.dashboard.DashboardModelResource")) {
			return "dashboard";
		} else {
			return "reportUnit";
		}
	}
}
