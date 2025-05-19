package com.mulfarsh.dhj.basaldb.mybatisflex.config;

import com.mulfarsh.dhj.basaldb.initialize.DataSourceConfig;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.datasource.FlexDataSource;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataSource.class)
@RequiredArgsConstructor
public class MybatisFlexDatasourceConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final DataSourceConfig dataSourceConfig;

    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent event) {
        FlexGlobalConfig defaultConfig = FlexGlobalConfig.getDefaultConfig();
        if (defaultConfig == null) {
            return;
        }
        FlexDataSource flexDataSource = defaultConfig.getDataSource();
        if (flexDataSource == null) {
            return;
        }
        String defaultDataSourceKey = flexDataSource.getDefaultDataSourceKey();
        flexDataSource.removeDatasource(defaultDataSourceKey);
        dataSourceConfig.dataSourceMap().forEach((name, datasource) -> {
            flexDataSource.addDataSource(name, datasource);
            if (datasource.equals(dataSourceConfig.defaultDataSource())) {
                flexDataSource.setDefaultDataSource(name);
            }
        });
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
