package com.mulfarsh.dhj.basaldb.mybatisflex.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.datasource.FlexDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MybatisFlexDatasourceConfig {

    private final DataSource userCenterDataSource;
    private final DataSource partyFeeDataSource;
    private final DataSource partyBuildingDataSource;

    public void configDatasources() {
        final FlexDataSource dataSource = FlexGlobalConfig.getDefaultConfig().getDataSource();
        dataSource.addDataSource("user-center", userCenterDataSource);
        dataSource.addDataSource("party-fee", partyFeeDataSource);
        dataSource.addDataSource("party-building", partyBuildingDataSource);

    }
}
