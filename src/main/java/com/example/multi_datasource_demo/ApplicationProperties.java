package com.example.multi_datasource_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties {
    private List<String> datasourceList;
}
