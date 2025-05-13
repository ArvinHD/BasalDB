package com.mulfarsh.dhj.basaldb.beansearcher.extension;

import cn.zhxu.bs.FieldOp;
import cn.zhxu.bs.util.FieldFns;
import lombok.Data;

@Data
public class BSBuilderField<T>{
    public static <T>BSBuilderField of(BSBuilderOperator<T> operator, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
        return new BSBuilderField(operator, fieldFn, values);
    }

    public static <T>BSBuilderField of(Class<? extends FieldOp> fieldOp, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
        return new BSBuilderField(fieldOp, fieldFn, values);
    }

    public static <T> BSBuilderField or(BSBuilderOperator<T> operator, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
        return new Or<T>(operator, fieldFn, values);
    }

    public static <T>BSBuilderField or(Class<? extends FieldOp> fieldOp, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
        return new Or<T>(fieldOp, fieldFn, values);
    }

    private BSBuilderOperator<T>operator;
    private Class<? extends FieldOp> fieldOp;
    private FieldFns.FieldFn<T, ?>fieldFn;
    private Object[] values;

    BSBuilderField(BSBuilderOperator<T>operator, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
        this.operator = operator;
        this.fieldFn = fieldFn;
        this.values = values;
    }

    BSBuilderField(Class<? extends FieldOp> fieldOp, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
        this.fieldOp = fieldOp;
        this.fieldFn = fieldFn;
        this.values = values;
    }

    public static class Or<T>extends BSBuilderField<T>{

        Or(BSBuilderOperator<T>operator, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
            super(operator, fieldFn, values);
        }

        Or(Class<? extends FieldOp> fieldOp, FieldFns.FieldFn<T, ?>fieldFn, Object... values) {
            super(fieldOp, fieldFn, values);
        }
    }

}