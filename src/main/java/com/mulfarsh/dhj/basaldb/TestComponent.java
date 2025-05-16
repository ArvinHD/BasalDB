package com.mulfarsh.dhj.basaldb;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@Data
public class TestComponent {

    private String abc = "abc";
}
