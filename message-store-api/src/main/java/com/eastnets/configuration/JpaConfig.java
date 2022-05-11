package com.eastnets.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.eastnets.util.Constants;

@Component
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages = "com.eastnets")
public class JpaConfig {
	//
	// extends JpaBaseConfiguration
	// protected JpaConfig(DataSource dataSource, JpaProperties properties,
	// ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
	// super(dataSource, properties, jtaTransactionManager);
	// // TODO Auto-generated constructor stub
	// }

	@Autowired
	private DataSources dataSourceConfig;

	@Autowired
	private AppConfig appConfig;

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<>();
		if (appConfig.getDbConfig().getDbType().equals(Constants.DATABASE_TYPE_ORACLE)) {
			properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		} else {
			properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
		}
		final LocalContainerEntityManagerFactoryBean ret = builder.dataSource(dataSourceConfig.getDbDatasource())
				.properties(properties).packages("com.eastnets").persistenceUnit("test").build();
		
		return ret;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	// @Override
	// protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
	// EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
	// return adapter;
	// }
	//
	// @Override
	// protected Map<String, Object> getVendorProperties() {
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("jpaDialect",
	// "org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect");
	// return map;
	// }

}
