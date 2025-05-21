package com.mulfarsh.dhj.basaldb.initialize;

import com.sun.source.doctree.SeeTree;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "dhj-db.datasource")
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@Data
public class DataSourceConfig {

    @Data
    public static class DBProperties {

        private String url;
        private String username;
        private String password;
        private String driveClassName;
        private String type;
        private Boolean primary;
    }

    private Map<String, DBProperties> prop;
    private Map<String, DataSource> dataSourceMap = new HashMap<>();
    private DataSource defaultDataSource;

    @Bean
    public Map<String, DataSource> dataSourceMap() {
        return dataSourceMap;
    }

    @Bean
    public DataSource defaultDataSource() {
        return defaultDataSource;
    }

    @PostConstruct
    public void init() {
        prop.forEach((name, props) -> {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(props.getUrl());
            dataSource.setUsername(props.getUsername());
            dataSource.setPassword(props.getPassword());
            dataSource.setDriverClassName(props.getDriveClassName());
            dataSourceMap.put(name, dataSource);
            if (props.getPrimary() && defaultDataSource == null) {
                defaultDataSource = dataSource;
            }
        });
    }

}
