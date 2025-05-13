package com.mulfarsh.dhj.basaldb.mybatisflex.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration("MybatisFlexCustomConfiguration")
@RequiredArgsConstructor
public class CustomConfiguration implements MyBatisFlexCustomizer {


    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        configDataSource(globalConfig);
    }

    private void configDataSource(FlexGlobalConfig globalConfig) {

    }
}
