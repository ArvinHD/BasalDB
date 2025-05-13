package com.mulfarsh.dhj.basaldb.beansearcher.convertor.base;

import cn.zhxu.bs.FieldConvertor;
import cn.zhxu.bs.FieldMeta;

public class BSFieldConvertor implements FieldConvertor.BFieldConvertor {

    private Convertors convertors;

    @Override
    public boolean supports(FieldMeta meta, Class<?> valueType) {
        convertors = Convertors.getConvertor(meta, valueType);
        return convertors != null;
    }

    @Override
    public Object convert(FieldMeta meta, Object value) {
        return convertors.getValue(meta, value);
    }
}
