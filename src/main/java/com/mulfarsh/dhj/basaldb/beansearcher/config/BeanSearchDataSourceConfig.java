package com.mulfarsh.dhj.basaldb.beansearcher.config;

import cn.zhxu.bs.SqlInterceptor;
import cn.zhxu.bs.boot.NamedDataSource;
import com.mulfarsh.dhj.basaldb.initialize.DataSourceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BeanSearchDataSourceConfig {

    private final DataSourceConfig dataSourceConfig;

    @Bean
    public SqlInterceptor sqlInterceptor() {
        return new BSSqlInterceptor();
    }

    @Bean
    public List<NamedDataSource> namedDataSources() {
        List<NamedDataSource> namedDataSources = new ArrayList<>();
        dataSourceConfig.dataSourceMap().forEach((name, dataSource) -> {
            NamedDataSource namedDataSource = new NamedDataSource(name, dataSource);
            namedDataSources.add(namedDataSource);
        });
        return namedDataSources;
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
