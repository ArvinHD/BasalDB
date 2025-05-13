package com.mulfarsh.dhj.basaldb.beansearcher.extension;

import cn.zhxu.bs.util.FieldFns;
import com.mulfarsh.dhj.basaldb.beansearcher.proxy.ProxyMapBuilder;

import java.io.Serializable;

@FunctionalInterface
public interface BSBuilderOperator<T> extends Serializable {
    void exec(ProxyMapBuilder builder, FieldFns.FieldFn<T, ?> fieldFn, Object... values);

}