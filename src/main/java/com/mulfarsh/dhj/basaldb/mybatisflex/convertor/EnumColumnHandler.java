package com.mulfarsh.dhj.basaldb.mybatisflex.convertor;

import cn.hutool.core.util.ReflectUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumColumnHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public EnumColumnHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        Object v = ReflectUtil.invoke(parameter, "getValue");
        if (v instanceof String) {
            ps.setString(i, (String) v);
        } else if (v instanceof Integer) {
            ps.setInt(i, (Integer) v);
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        Object v = ReflectUtil.invoke(parameter, "getValue");
        if (v instanceof String) {
            ps.setString(i, (String) v);
        } else if (v instanceof Integer) {
            ps.setInt(i, (Integer) v);
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object s = rs.getString(columnName);
        final Method instance = ReflectUtil.getMethodByName(type, "INSTANCE");
        return ReflectUtil.invoke(null, instance, s);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object s = rs.getString(columnIndex);
        final Method instance = ReflectUtil.getMethodByName(type, "INSTANCE");
        return ReflectUtil.invoke(null, instance, s);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        final Method instance = ReflectUtil.getMethodByName(type, "INSTANCE");
        return ReflectUtil.invoke(null, instance, s);
    }
}
