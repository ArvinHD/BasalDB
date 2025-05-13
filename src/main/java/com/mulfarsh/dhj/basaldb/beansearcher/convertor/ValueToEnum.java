package com.mulfarsh.dhj.basaldb.beansearcher.convertor;

import cn.hutool.core.util.ReflectUtil;
import cn.zhxu.bs.FieldMeta;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.BSConvert;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.Convertors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValueToEnum extends Convertors {

    private Method constructor;
    private Function<String, Integer> resultFun;

    public Boolean isValidType() {
        final Field field = this.getFieldMeta().getField();
        final Class<?> type = field.getType();
        if (!type.isEnum()) {
            return false;
        }
        Function<String, Integer> resultFun =  Integer::parseInt;
        final Field valueFiled = ReflectUtil.getField(type, "value");
        List<Class> classes = new ArrayList<>();
        if (this.getAnnotation().allowTypes() != null && this.getAnnotation().allowTypes().length != 0) {
            classes.addAll(Arrays.stream(this.getAnnotation().allowTypes()).collect(Collectors.toList()));
        }
        if (!this.getAnnotation().originType().equals(Void.class)) {
            classes.add(this.getAnnotation().originType());
        }
        if (valueFiled != null && classes.contains(this.getValueType())) {
            constructor = ReflectUtil.getMethod(type, "INSTANCE", valueFiled.getType());
            configShuntFunc(valueFiled.getType());
            return constructor != null;
        }
        return false;
    };

    public ValueToEnum(BSConvert annotation, FieldMeta fieldMeta, Class<?> valueType) {
        super(annotation, fieldMeta, valueType);
    }

    @Override
    public Object getValue(FieldMeta fieldMeta, Object value) {
        Integer arg = null;
        if (resultFun != null) {
            arg = (Integer) resultFun.apply((String) value);
        } else {
            arg = ((Number) value).intValue();
        }
        return ReflectUtil.invokeStatic(constructor, arg);
    }

    private void configShuntFunc(Class<?> filedType) {
        if (Number.class.isAssignableFrom(filedType) && this.getValueType().equals(String.class)) {
            resultFun = Integer::parseInt;
        }
    }
}
