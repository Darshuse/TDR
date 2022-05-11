package com.eastnets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.configuration.DaoFactoryConfig;
import com.eastnets.entities.LdErrors;

@Service
public class LdErrorsServiceImpl implements LdErrorsService {

	
	
	@Autowired
	private DaoFactoryConfig daoFactoryConfig;

	@Override
	public void save(LdErrors ldErrors) {

		daoFactoryConfig.getCommonDAOImpl().insertLdErrors(ldErrors);

	}
}
