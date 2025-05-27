package com.mulfarsh.dhj.basaldb.mybatisflex.generator;

import com.mybatisflex.codegen.config.ColumnConfig;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MFGeneratorConfig {

    private String dataSource;
    private String prefix;
    private String suffix;
    private String basePackage;
    private String entitySuffix = "Entity";
    private String mapperSuffix = "Mapper";
    private String serviceSuffix = "BO";
    private String implSuffix = "BOImpl";
    private Class<?> baseEntity;
    private Class<?> baseMapper;
    private Class<?> baseService;
    private Class<?> baseImpl;
    private String []tablePrefixes;
    private String []generateTables;
    private String []excludeTables;
    private String []ignoreColumns;
    private Map<String, ColumnConfig> columnConfigs;
}
