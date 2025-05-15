package com.mulfarsh.dhj.basaldb.initialize;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

//@Component
@ConfigurationProperties(prefix = "dhj-db")
@Data
public class DataSourceProperties {

    private Map<String, Map<String, String>> datasource;
}
