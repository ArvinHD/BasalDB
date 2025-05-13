package com.mulfarsh.dhj.basaldb.beansearcher.extension;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.SearchBean;
import cn.zhxu.bs.util.FieldFns;

import java.lang.reflect.Field;

public class BSColumnFinder {

    public static <T> String column(Class<T> beanClass, FieldFns.FieldFn<T, ?> fieldFn) {
        if (!AnnotationUtil.hasAnnotation(beanClass, SearchBean.class)) {
            return null;
        }
        SearchBean searchBean = AnnotationUtil.getAnnotation(beanClass, SearchBean.class);
        String defaultTable = "";
        if (StrUtil.isNotEmpty(searchBean.autoMapTo())) {
            defaultTable = searchBean.autoMapTo();
        }
        String fieldName = FieldFns.name(fieldFn);
        if (StrUtil.isEmpty(fieldName)) {
            return null;
        }
        Field field = ReflectUtil.getField(beanClass, fieldName);
        String columnName = fieldName;
        if (AnnotationUtil.hasAnnotation(field, DbField.class)) {
            DbField dbField = AnnotationUtil.getAnnotation(field, DbField.class);
            columnName = dbField.value();
        }
        if (!columnName.contains(".") && StrUtil.isNotEmpty(defaultTable)) {
            columnName = defaultTable + "." + columnName;
        }
        return columnName;
    }
}
