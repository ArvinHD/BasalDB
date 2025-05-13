package com.mulfarsh.dhj.basaldb.mybatisflex.external;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutocompleteDataModel {

    @AliasFor("turnOn")
    boolean value() default false;

    @AliasFor("value")
    boolean turnOn() default false;
}
