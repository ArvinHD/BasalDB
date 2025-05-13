package com.mulfarsh.dhj.basaldb.beansearcher.convertor.base;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BSConvert {

    Class<?> convertor();

    Class<?> originType() default Void.class;

    BSConvertorType convertorType() default BSConvertorType.NONE;

    Class<?> []allowTypes() default {};

}
