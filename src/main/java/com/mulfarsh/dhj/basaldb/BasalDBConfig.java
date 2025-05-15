package com.mulfarsh.dhj.basaldb;

import com.mulfarsh.dhj.basaldb.initialize.DataSourceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfiguration
@ComponentScan(basePackages = "com.mulfarsh.dhj.basaldb")
//@ConfigurationPropertiesScan(basePackages = "com.mulfarsh.dhj.basaldb")
//@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
//@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(DataSourceProperties.class)
//@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class BasalDBConfig {

}
