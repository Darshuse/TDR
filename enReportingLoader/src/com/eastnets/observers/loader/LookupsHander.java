package com.eastnets.observers.loader;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.loader.Inst;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.Mesg;

public class LookupsHander implements Observer {

	private List<String> mesgFormatList ;
	private List<String> mesgNatureList;
	private List<String> mesgQualifierList;
	private List<String> rpNameList;
	private List<String> unitNameList;
	private LoaderDAO loaderDaoImp;
	
	
	public void init(){
		
		mesgFormatList = getLoaderDaoImp().getMesgFormat();
		mesgNatureList = getLoaderDaoImp().getMesgNature();
		mesgQualifierList = getLoaderDaoImp().getMesgQualifier();
		rpNameList = getLoaderDaoImp().getMesgRPName();
		unitNameList = getLoaderDaoImp().getMesgUnitName();
		
	}
	
	
	@Override
	public void update(Observable arg0, Object arg1) {

		  if (arg1 instanceof LoaderMessage) {
			  LoaderMessage loaderMesg = (LoaderMessage) arg1;
			  Mesg newMesg = loaderMesg.getMesg();
			  if(newMesg.getMesgFrmtName()!=null && !mesgFormatList.contains(newMesg.getMesgFrmtName())){
				  mesgFormatList.add(newMesg.getMesgFrmtName());
				  loaderDaoImp.addMesgFormat(newMesg.getMesgFrmtName());
			  }if(newMesg.getMesgNature()!=null && !mesgNatureList.contains(newMesg.getMesgNature())){
				  mesgNatureList.add(newMesg.getMesgNature());
				  loaderDaoImp.addMesgNature(newMesg.getMesgNature());
			  }if(newMesg.getMesgMesgUserGroup()!=null && !mesgQualifierList.contains(newMesg.getMesgMesgUserGroup())){// Qualifier Name
				  mesgQualifierList.add(newMesg.getMesgMesgUserGroup());
				  loaderDaoImp.addMesgQualifier(newMesg.getMesgMesgUserGroup());
			  }
			  
			  updateRPName(newMesg.getRinsts());
			  updateUnitName(newMesg.getRinsts());
		}
	}

	private void updateRPName(List<Inst> rinsts) {

		if(rinsts == null || rinsts.isEmpty()){
			return;
		}
		for(Inst inst : rinsts){
			 if(inst.getInstRpName() != null && !rpNameList.contains(inst.getInstRpName())){
				 rpNameList.add(inst.getInstRpName());
				  loaderDaoImp.addMesgRPName(inst.getInstRpName());
			  }
		}
	}
	
	private void updateUnitName(List<Inst> rinsts) {

		if(rinsts == null || rinsts.isEmpty()){
			return;
		}
		for(Inst inst : rinsts){
			if(inst.getInstUnitName()!=null && !unitNameList.contains(inst.getInstUnitName())){
				unitNameList.add(inst.getInstUnitName());
				  loaderDaoImp.addMesgUnitName(inst.getInstUnitName());
				  
			  }
		}
		
	}

	public List<String> getMesgNatureList() {
		return mesgNatureList;
	}


	public void setMesgNatureList(List<String> mesgNatureList) {
		this.mesgNatureList = mesgNatureList;
	}


	public List<String> getMesgQualifierList() {
		return mesgQualifierList;
	}


	public void setMesgQualifierList(List<String> mesgQualifierList) {
		this.mesgQualifierList = mesgQualifierList;
	}


	public List<String> getRpNameList() {
		return rpNameList;
	}


	public void setRpNameList(List<String> rpNameList) {
		this.rpNameList = rpNameList;
	}


	public List<String> getUnitNameList() {
		return unitNameList;
	}


	public void setUnitNameList(List<String> unitNameList) {
		this.unitNameList = unitNameList;
	}


	public List<String> getMesgFormatList() {
		return mesgFormatList;
	}


	public void setMesgFormatList(List<String> mesgFormatList) {
		this.mesgFormatList = mesgFormatList;
	}


	public LoaderDAO getLoaderDaoImp() {
		return loaderDaoImp;
	}


	public void setLoaderDaoImp(LoaderDAO loaderDaoImp) {
		this.loaderDaoImp = loaderDaoImp;
	}
	

}
