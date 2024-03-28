package com.example.multi_datasource_demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import javax.sql.DataSource;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableJpaRepositories
@EnableTransactionManagement
/*
 * 参考：https://spring.pleiades.io/spring-data/jpa/reference/repositories/create-instances.html
 */
public class DataSourceConfig {

    @Autowired private ApplicationProperties applicationProperties;
    @Autowired
    Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        var entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan("com.example.multi_datasource_demo");
        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        var jpaPropertyMap = new HashMap<String, String>();
        jpaPropertyMap.put("hibernate.dialect", PostgreSQLDialect.class.getName());
        entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap);
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public DataSource dataSource() {
        var dataSourceMap = new HashMap<>();
        for (var dataSource : applicationProperties.getDatasourceList()) {
            dataSourceMap.put(dataSource, makeDataSource(dataSource));
        }
        var router = new DynamicDataSource();
        router.setTargetDataSources(dataSourceMap);
        return router;
    }

    private DataSource makeDataSource(String key) {
        var config = new HikariConfig();
        config.setDriverClassName(env.getProperty("spring.datasource." + key + ".driver"));
        config.setJdbcUrl(env.getProperty("spring.datasource." + key + ".url"));
        config.setUsername(env.getProperty("spring.datasource." + key + ".username"));
        config.setPassword(env.getProperty("spring.datasource." + key + ".password"));
        return new HikariDataSource(config);
    }
}
