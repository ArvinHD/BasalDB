package com.mulfarsh.dhj.basaldb.mybatisflex.convertor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.table.TableInfoFactory;
import lombok.Getter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonToEnumsTypeHandler<E extends Enum<E>> extends BaseTypeHandler<List<E>> {

    private SplitColumnHandler splitHandler = new SplitColumnHandler();
    @Getter
    private  String seq;
    private Class<E> eClass;

    public JsonToEnumsTypeHandler(Class eClass) {

    }

    public void setSeq(String seq) {
        this.seq = seq;
        splitHandler.setSeq(seq);
    }

    public String getSeq() {
        return seq;
    }

    public static class BackslashJsonHandler<E extends Enum<E>> extends JsonToEnumsTypeHandler<E> {

        public BackslashJsonHandler(Class eClass) {
            super(eClass);
            setSeq("/");
        }
    }

    public static class CommaJsonHander<E extends Enum<E>> extends JsonToEnumsTypeHandler<E> {
        public CommaJsonHander(Class eClass) {
            super(eClass);
            setSeq(",");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<E> parameter, JdbcType jdbcType) throws SQLException {
        Method getValue = ReflectUtil.getMethodByName(eClass, "getValue");
        List<String> strings = parameter.stream()
                .map(e -> String.valueOf((Integer) ReflectUtil.invoke(e, getValue, null)))
                .collect(Collectors.toList());
        if (parameter != null) {
            ps.setString(i, String.join(seq, strings));
        } else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public List<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {

        final String tableName = rs.getMetaData().getTableName(1);
        final TableInfo tableInfo = TableInfoFactory.ofTableName(tableName);
        final Map<String, String> propertyColumnMapping = tableInfo.getPropertyColumnMapping();
        final Class<?> entityClass = tableInfo.getEntityClass();
        final Field field = ReflectUtil.getField(entityClass, propertyColumnMapping.get(columnName));
        ColumnRawType columnRawType = field.getAnnotation(ColumnRawType.class);
        Class eClass = columnRawType.rawTyep();
        this.eClass = eClass;
        return splitByComma(rs.getString(columnName));

    }

    @Override
    public List<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return splitByComma(rs.getString(columnIndex));
    }

    @Override
    public List<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return splitByComma(cs.getString(columnIndex));
    }

    private List<E> splitByComma(String string) {
        if (string == null || string.isEmpty() || !JSONUtil.isTypeJSON(string)) {
            return null;
        }
        final Method instance = ReflectUtil.getMethodByName(eClass, "INSTANCE");
        return JSONUtil.parseArray(string)
                .toList(Integer.class)
                .stream()
                .map(i -> {
                    return (E) ReflectUtil.invoke(null, instance, i);
                })
                .collect(Collectors.toList());
    }

}
