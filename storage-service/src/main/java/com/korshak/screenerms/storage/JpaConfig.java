package com.korshak.screenerms.storage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.korshak.screenerms.storage.dao")
@EntityScan(basePackages = "com.korshak.screenerms.storage.dao")
@ConfigurationProperties(prefix = "spring.datasource")
public class JpaConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
