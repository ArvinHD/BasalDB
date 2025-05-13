package com.mulfarsh.dhj.basaldb.beansearcher.operator;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.BeanMeta;
import cn.zhxu.bs.SqlSnippet;
import cn.zhxu.bs.util.FieldFns;
import com.mulfarsh.dhj.basaldb.beansearcher.extension.BSColumnFinder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class GroupBy<T> {

    public static final String GROUP_BY_KEY = GroupBy.class.getName();
    private static final String GROUP_BY_FIELD = "groupBySnippet";

    public static <T> GroupBy<T> COVER(boolean isCover) {
        return new GroupBy<>(isCover);
    }

    public static <T> GroupBy<T> FIELDS(FieldFns.FieldFn<T, ?> ... fieldFns) {
        return new GroupBy<>(fieldFns);
    }

    public static <T> GroupBy<T> gropyBy(BeanMeta<T> beanMeta, Map<String, Object> paramMap) {
        if (!paramMap.containsKey(GROUP_BY_KEY)) {
            return null;
        }
        GroupBy<T> groupBy = (GroupBy<T>) paramMap.get(GROUP_BY_KEY);
        paramMap.remove(GROUP_BY_KEY);
        SqlSnippet sqlSnippet = groupBy.configGroupBy(beanMeta, paramMap);
        if (sqlSnippet != null) {
            ReflectUtil.setFieldValue(beanMeta, GROUP_BY_FIELD, sqlSnippet);
            groupBy.sql = sqlSnippet;
        }
        return groupBy;
    }

    public enum FieldType {
        STRING,
        FIELD_FN
    }


    @Getter
    private boolean conver = false;
    @Getter
    private String []fields;
    @Getter
    private FieldFns.FieldFn<T, ?> []fieldFns;
    @Getter
    private FieldType fieldType;

    @Getter
    SqlSnippet sql;
    @Getter
    private boolean complete = false;

    public GroupBy(String fields) {
        this(false, fields);
    }

    private GroupBy() {}

    public GroupBy(boolean conver, String fields) {
        this(conver, StrUtil.split(fields, ",").toArray(new String[0]));
    }

    public GroupBy(String ... fields) {
        this(false, fields);
    }

    public GroupBy(boolean conver, String ... fields) {
        this.conver = conver;
        this.fields = fields;
        this.fieldType = FieldType.STRING;
    }

    public GroupBy(FieldFns.FieldFn<T, ?>... fieldFns) {
        this(false, fieldFns);
    }

    public GroupBy(boolean conver, FieldFns.FieldFn<T, ?> ... fieldFns) {
        this.conver = conver;
        this.fieldFns = fieldFns;
        this.fieldType = FieldType.FIELD_FN;
    }

    public GroupBy(List<Object> fields, FieldType fieldType) {
        this(false, fields, fieldType);
    }

    public GroupBy(boolean isCover) {
        this.conver = isCover;
    }

    public GroupBy(boolean conver, List<Object> fields, FieldType fieldType) {
        this();
        this.conver = conver;
        this.fieldType = fieldType;
        switch (fieldType) {
            case STRING:
                this.fields = fields.toArray(new String[0]);
                break;
            case FIELD_FN:
                this.fieldFns = fields.toArray(new FieldFns.FieldFn[0]);
                break;
        }
    }



    private SqlSnippet configGroupBy(BeanMeta<T> beanMeta, Map<String, Object> paraMap) {

        StringBuilder stringBuilder = new StringBuilder();
        switch (getFieldType()) {
            case STRING:
                for (String field : getFields()) {
                    if (StrUtil.isNotEmpty(stringBuilder.toString())) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(field);
                }
                break;
            case FIELD_FN:
                for (FieldFns.FieldFn<T, ?> fieldFn : getFieldFns()) {
                    String column = BSColumnFinder.column(beanMeta.getBeanClass(), fieldFn);
                    if (StrUtil.isEmpty(column)) {
                        continue;
                    }
                    if (StrUtil.isNotEmpty(stringBuilder.toString())) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("  ");
                    stringBuilder.append(column);
                }
                break;
        }
        if (isConver()) {
            return new SqlSnippet(stringBuilder.toString().replace("\n", ""));
        } else {
            String origin = StrUtil.isNotEmpty(beanMeta.getGroupBy()) ? beanMeta.getGroupBy() + ", " : "";
            stringBuilder.insert(0, origin);
            return new SqlSnippet(stringBuilder.toString().replace("\n", ""));
        }
    }

}
