package com.mulfarsh.dhj.basaldb;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfiguration
public class TestAutoScan {

    @Bean(name = "testInteger")
    public Integer testInteger() {
        System.out.println(2);
        return 2;
    }

    @Bean(name = "testScan")
    public String testScan() {
        System.out.println("Auto Sacn Success");
        return "testScan";
    }

}
