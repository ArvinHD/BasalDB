package com.mulfarsh.dhj.basaldb.beansearcher.convertor.base;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.zhxu.bs.FieldMeta;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.NumberToString;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.ValueToEnum;
import lombok.Getter;

import java.lang.reflect.Field;

public abstract class Convertors {

    @Getter
    private BSConvert annotation;
    @Getter
    private FieldMeta fieldMeta;
    @Getter
    private Class<?> valueType;

    public Convertors(BSConvert annotation, FieldMeta fieldMeta, Class<?> valueType) {
        this.annotation = annotation;
        this.fieldMeta = fieldMeta;
        this.valueType = valueType;
    }

    static  <T extends Convertors> T getConvertor(FieldMeta fieldMeta, Class<?> valueType) {
        Field field = fieldMeta.getField();
        if (field == null) {
            return null;
        }
        BSConvert annotation = AnnotationUtil.getAnnotation(field, BSConvert.class);
        if (annotation == null) {
            return null;
        }
        if (!annotation.originType().equals(Void.class) && !valueType.equals(annotation.originType())) {
            return null;
        }
        T convertors = null;
        switch (annotation.convertorType()) {
            case NONE:
                return null;
            case NUMBER_TO_STRING:
                convertors = (T) new NumberToString(annotation, fieldMeta, valueType);
                break;
            case VALUE_TO_ENUM:
                convertors = (T) new ValueToEnum(annotation, fieldMeta, valueType);
                break;
            case TO_DATE:
                convertors = (T) ReflectUtil.newInstance(annotation.convertor(), annotation, fieldMeta, valueType);
                break;
        }
        if (convertors != null && convertors.isValidType()) {
            return convertors;
        }
        return null;
    }


    public abstract Object getValue(FieldMeta fieldMeta, Object value);
    public abstract Boolean isValidType();
}
