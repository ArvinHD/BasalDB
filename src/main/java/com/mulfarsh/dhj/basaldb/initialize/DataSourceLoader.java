package com.mulfarsh.dhj.basaldb.initialize;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.Map;

@Component(value = "dhj-basal-db-datasource-loader")
public class DataSourceLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 获取Spring上下文
        Map<String, DataSource> dataSources = event.getApplicationContext().getBeansOfType(DataSource.class);
        System.out.println("DB 模块");
        // 遍历输出所有数据源
        dataSources.forEach((name, dataSource) -> {
            System.out.println("数据源名称: " + name);
            System.out.println("数据源实例: " + dataSource);
            // 你可以在这里对数据源进行后续操作
        });
    }
}