package com.eastnets.textbreak.bean;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.persistence.config.BatchWriting;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.eastnets.textbreak.repositories", entityManagerFactoryRef = "tbEntityManagerFactory", transactionManagerRef = "tbTransactionManager")
public class CustomJpaConfig extends JpaBaseConfiguration {

	@Autowired
	TextBreakConfig textBreakConfig;

	protected CustomJpaConfig(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		super(dataSource, properties, jtaTransactionManager);
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new EclipseLinkJpaVendorAdapter();
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		final Map<String, Object> ret = new HashMap<>();
		ret.put(PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC);
		ret.put("jpaDialect", "org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect");
		return ret;
	}

	@Bean("tbEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean build = builder.dataSource(dataSource).packages("com.eastnets.textbreak.entities").persistenceUnit("persistenceUnitTB").properties(initJpaProperties()).build();
		build.setJpaDialect(new EclipseLinkJpaDialect());
		build.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
		return build;
	}

	@Bean("tbTransactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	private Map<String, ?> initJpaProperties() {
		final Map<String, Object> ret = new HashMap<>();
		// Add any JpaProperty you are interested in and is supported by your Database and JPA implementation
		ret.put(PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC);
		ret.put(PersistenceUnitProperties.BATCH_WRITING_SIZE, "1000");
		ret.put(PersistenceUnitProperties.WEAVING, "false");
		ret.put(PersistenceUnitProperties.LOGGING_LEVEL, "OFF");
		// ret.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
		ret.put(PersistenceUnitProperties.DDL_GENERATION_MODE, "database");
		return ret;
	}

}