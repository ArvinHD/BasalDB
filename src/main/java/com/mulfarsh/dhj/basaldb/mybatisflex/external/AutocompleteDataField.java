package com.mulfarsh.dhj.basaldb.mybatisflex.external;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutocompleteDataField {

    @AliasFor("turnOn")
    boolean value() default true;

    @AliasFor("value")
    boolean turnOn() default true;

    AutocompleteField fieldType() default AutocompleteField.IGNORE;
}


