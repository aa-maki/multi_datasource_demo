package com.example.multi_datasource_demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
public class DbConfig {

  @Autowired ApplicationConfig applicationConfig;

  @Autowired Environment env;

  @Bean
  public DataSource dataSource() {
    var dataSourceMap = new HashMap<>();
    for (var dataSource : applicationConfig.getDatasourceList()) {
      dataSourceMap.put(dataSource, makeDataSource(dataSource));
    }

    var router = new DynamicDataSource();
    router.setTargetDataSources(dataSourceMap);
    return router;
  }

  private DataSource makeDataSource(String key) {
    var config = new HikariConfig();
    config.setDriverClassName(env.getProperty("datasource." + key + ".driver"));
    config.setJdbcUrl(env.getProperty("datasource." + key + ".url"));
    config.setUsername(env.getProperty("datasource." + key + ".username"));
    config.setPassword(env.getProperty("datasource." + key + ".password"));
    config.addDataSourceProperty("hibernate.hbm2ddl.auto", "update");
    return new HikariDataSource(config);
  }
}
