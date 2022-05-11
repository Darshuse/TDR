package com.eastnets.textbreak.dao;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.eastnets.resultbean.TextBreakResultBean;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.dao.beans.User;
import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;

public interface TeacBreakDao {

	public void fillTextField(List<TextField> textFields, Date xCreationDateTime);

	public void fillTextLoop(List<TextFieldLoop> textFieldLoopList, Date xCreationDateTime);

	public void fillSystemTextField(List<SystemTextField> systemTextFields, Date xCreationDateTime);

	public void updateParsinStatus(SourceData sourceData, Integer decopsedStatus);

	public void updateFinMessageStatus(List<TextField> textFields, Integer decopsedStatus);

	public void updateSysMessageStatus(List<SystemTextField> systemTextFields, Integer decopsedStatus);

	public User getUserProfileId(String username);

	public List<String> getProfileRoles(Long profileID);

	public boolean sCheckRoals();

	public void insertLdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2);

	public List<TextBreakResultBean> getAllMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber);

	public List<TextBreakResultBean> getConfiguredMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber);

	public List<TextBreakResultBean> getRecoveryMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, Integer messageNumber);

	public List<TextBreakResultBean> getSelectedOnlineTextBreakMessage(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber);

}
