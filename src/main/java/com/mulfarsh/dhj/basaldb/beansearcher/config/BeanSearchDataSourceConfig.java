package com.mulfarsh.dhj.basaldb.beansearcher.config;

import cn.zhxu.bs.SqlInterceptor;
import cn.zhxu.bs.boot.NamedDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BeanSearchDataSourceConfig {

    @Bean
    public SqlInterceptor sqlInterceptor() {
        return new BSSqlInterceptor();
    }
//
//    @Bean
//    public NamedDataSource partyBuildingNamedDataSource(@Qualifier("partyBuildingDataSource") DataSource dataSource) {
//        return new NamedDataSource("party-building", dataSource);
//    }
//
//    @Bean
//    public NamedDataSource partyFeeNamedDataSource(@Qualifier("partyFeeDataSource") DataSource dataSource) {
//        return new NamedDataSource("party-fee", dataSource);
//    }
//
//    @Bean
//    public NamedDataSource userCenterNamedDataSource(@Qualifier("userCenterDataSource") DataSource dataSource) {
//        return new NamedDataSource("user-center", dataSource);
//    }

}
