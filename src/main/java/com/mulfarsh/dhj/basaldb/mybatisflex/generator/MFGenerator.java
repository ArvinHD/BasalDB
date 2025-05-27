package com.mulfarsh.dhj.basaldb.mybatisflex.generator;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.mulfarsh.dhj.basaldb.initialize.DataSourceConfig;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author vansd
 * @since 2025/5/27 20:29
 */
@Component
@RequiredArgsConstructor
public class MFGenerator {

    private static MFGenerator INSTANCE;

    public static void generate(MFGeneratorConfig config) {
        DataSource dataSource = INSTANCE.config.getDataSourceMap().getOrDefault(config.getDataSource(), null);
        if (dataSource == null) {
            return;
        }
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBasePackage(config.getBasePackage());
        globalConfig.setAuthor("ArvinHD");
        globalConfig.setSince(DateUtil.now());

        EntityConfig entityConfig = globalConfig.enableEntity();
        MapperConfig mapperConfig = globalConfig.enableMapper();
        ServiceConfig serviceConfig = globalConfig.enableService();
        ServiceImplConfig serviceImplConfig = globalConfig.enableServiceImpl();
        entityConfig
                .setWithLombok(true)
                .setDataSource(config.getDataSource())
                .setWithLombok(true)
                .setJdkVersion(17);
        if (StrUtil.isNotEmpty(config.getPrefix())) {
            entityConfig.setClassPrefix(config.getPrefix());
        }
        if (StrUtil.isNotEmpty(config.getSuffix() + config.getEntitySuffix())) {
            entityConfig.setClassSuffix(config.getSuffix() + config.getEntitySuffix());
        }
        if (config.getBaseEntity() != null) {
            entityConfig.setWithBaseClassEnable(true);
            entityConfig.setSuperClass(config.getBaseEntity());
        }

        mapperConfig
                .setMapperAnnotation(true)
                .setClassPrefix(config.getPrefix())
                .setClassSuffix(config.getSuffix() + config.getMapperSuffix())
                .setSuperClass(config.getBaseMapper());

        serviceConfig
                .setClassPrefix(config.getPrefix())
                .setClassSuffix(config.getSuffix() + config.getServiceSuffix())
                .setSuperClass(config.getBaseService());


        serviceImplConfig
                .setClassPrefix(config.getPrefix())
                .setClassSuffix(config.getSuffix() + config.getImplSuffix())
                .setSuperClass(config.getBaseImpl());

        StrategyConfig strategyConfig = globalConfig.getStrategyConfig();
        if (ObjUtil.isNotEmpty(config.getTablePrefixes())) {
            strategyConfig.setTablePrefix(config.getTablePrefixes());
        }
        if (ObjUtil.isNotEmpty(config.getGenerateTables())) {
            strategyConfig.setGenerateTable(config.getGenerateTables());
        }
        if (ObjUtil.isNotEmpty(config.getExcludeTables())) {
            strategyConfig.setUnGenerateTable(config.getExcludeTables());
        }
        if (ObjUtil.isNotEmpty(config.getIgnoreColumns())) {
            strategyConfig.setIgnoreColumns(config.getIgnoreColumns());
        }
        config.getColumnConfigs().forEach((table, columnConfig) -> {
            globalConfig.setColumnConfig(table, columnConfig);
        });

        Generator generator = new Generator(dataSource, globalConfig);
        generator.generate();
    }

    private final DataSourceConfig config;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

}
