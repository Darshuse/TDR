package com.eastnets.textbreak.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.entities.GlobalSettings;
import com.eastnets.entities.LdTextbreakHistory;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.SystemTextField;
import com.eastnets.entities.TextField;
import com.eastnets.entities.TextFieldLoop;
import com.eastnets.repositories.GlobalSettingsRepository;
import com.eastnets.repositories.MesgRepository;
import com.eastnets.repositories.SystemTextFieldRepository;
import com.eastnets.repositories.TextBreakHistoryRepository;
import com.eastnets.repositories.TextFieldLoopRepository;
import com.eastnets.repositories.TextFieldRepository;
import com.eastnets.repositories.TextRepository;
import com.eastnets.resultbean.TextBreakResultBean;



@Service
public class TextBreakRepositoryService {

	@Autowired
	private MesgRepository mesgRepository;
	@Autowired
	private GlobalSettingsRepository globalSettingsRepository;
	@Autowired
	private TextFieldRepository fieldRepository;
	@Autowired
	private TextFieldLoopRepository fieldLoopRepository;
	@Autowired
	private TextRepository textRepository;
	@Autowired
	private SystemTextFieldRepository  systemTextFieldRepository;
	@Autowired
	private TextBreakHistoryRepository textBreakHistoryRepository;
	
	
	
	
	
	@Transactional()
	public void fillLdTextbreakHistories(LdTextbreakHistory ldTextbreakHistorie) {
		textBreakHistoryRepository.save(ldTextbreakHistorie);		
		//textBreakHistoryRepository.persist(ldTextbreakHistorie.getId().getAid(), ldTextbreakHistorie.getId().getUmidl(), ldTextbreakHistorie.getId().getUmidh());
	}	
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateText(int aid,Long umidh,Long umidl) {
		textRepository.updateText(aid, umidh, umidl);
	}
	
	
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateTextStatus(String status,int aid,Long umidh,Long umidl) {
		textRepository.updateTextStatus(status,aid, umidh, umidl);
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void fillTextField(List<TextField> textFields) {
		fieldRepository.saveAll(textFields); 
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void fillTextLoop(List<TextFieldLoop> fieldLoops) {
		fieldLoopRepository.saveAll(fieldLoops);
	}
	
	@Transactional(readOnly=true)
	public void fillSystemTextField(List<SystemTextField> systemTextFields) {
		systemTextFieldRepository.saveAll(systemTextFields);
	}
	
	
	@Transactional(readOnly=true)
	public List<Mesg> findAll() {
		return mesgRepository.findAll();
	}

	@Transactional(readOnly=true) 
	public List<Mesg> getSomeMessages() {
		return mesgRepository.findMesgByMesgTrnRefAndMesgType("02375","103");
	}

	/*	@Transactional(readOnly=true) 
	public List<Mesg> findTextBreakMessages(Date fromDate,Date toDate,String msgFrmtName,String aid) throws ParseException {
		return mesgRepository.getTextBreakMessage(fromDate,toDate,msgFrmtName,Integer.parseInt(aid));
	}*/

	@Transactional(readOnly=true) 
	public List<TextBreakResultBean> findTextBreakMessages(Date fromDate,Date toDate,String msgFrmtName,String aid,	Pageable pageable,boolean allMode) throws ParseException { 
		if(allMode == true){
			return mesgRepository.getAllTextBreakMessage(fromDate,toDate,msgFrmtName,Integer.parseInt(aid),pageable);
		}
		return mesgRepository.getTextBreakMessage(fromDate,toDate,msgFrmtName,Integer.parseInt(aid),pageable);
	}

	
	@Transactional(readOnly=true) 
	public List<TextBreakResultBean> findRestoreMessages(String msgFrmtName,String aid ) throws ParseException { 
		return mesgRepository.findRestoreMessages(msgFrmtName, Integer.parseInt(aid));
	}


	@Transactional(readOnly=true) 
	public List<GlobalSettings> getGlobelSettings()  {
		return globalSettingsRepository.findAll();
	}

	public TextFieldLoopRepository getFieldLoopRepository() {
		return fieldLoopRepository;
	}

	public void setFieldLoopRepository(TextFieldLoopRepository fieldLoopRepository) {
		this.fieldLoopRepository = fieldLoopRepository;
	}

	public TextRepository getTextRepository() {
		return textRepository;
	}

	public void setTextRepository(TextRepository textRepository) {
		this.textRepository = textRepository;
	}


	public SystemTextFieldRepository getSystemTextFieldRepository() {
		return systemTextFieldRepository;
	}


	public void setSystemTextFieldRepository(SystemTextFieldRepository systemTextFieldRepository) {
		this.systemTextFieldRepository = systemTextFieldRepository;
	}


	public TextBreakHistoryRepository getTextBreakHistoryRepository() {
		return textBreakHistoryRepository;
	}


	public void setTextBreakHistoryRepository(TextBreakHistoryRepository textBreakHistoryRepository) {
		this.textBreakHistoryRepository = textBreakHistoryRepository;
	}

}
