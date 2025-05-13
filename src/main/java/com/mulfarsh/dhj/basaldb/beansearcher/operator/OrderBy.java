package com.mulfarsh.dhj.basaldb.beansearcher.operator;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.BeanMeta;
import cn.zhxu.bs.SqlSnippet;
import cn.zhxu.bs.util.FieldFns;
import com.mulfarsh.dhj.basaldb.beansearcher.extension.BSColumnFinder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderBy<T> {

    public static final String ORDER_BY_KEY = OrderBy.class.getName();
    private static final String ORDER_BY_FIELD = "orderBySnippet";

    public static <T> OrderBy<T> ASC(FieldFns.FieldFn<T, ?>... fieldFns) {
        final OrderBy<T> orderBy = new OrderBy<>(SortType.ASC, fieldFns);
        return orderBy;
    }

    public static <T> OrderBy<T> DESC(FieldFns.FieldFn<T, ?>... fieldFns) {
        return new OrderBy<>(SortType.DESC, fieldFns);
    }

    public static <T> OrderBy<T> CONVER(boolean isConver) {
        return new OrderBy<>(isConver);
    }


    public static <T> OrderBy<T> orderBy(BeanMeta<T> beanMeta, Map<String, Object> paramMap) {
        if (!paramMap.containsKey(ORDER_BY_KEY)) {
            return null;
        }
        OrderBy<T> orderBy = (OrderBy<T>) paramMap.get(ORDER_BY_KEY);
        paramMap.remove(ORDER_BY_KEY);
        SqlSnippet sqlSnippet = orderBy.configOrderBy(beanMeta, paramMap);
        if (sqlSnippet != null) {
            ReflectUtil.setFieldValue(beanMeta, ORDER_BY_FIELD, sqlSnippet);
            orderBy.sql = sqlSnippet;
            orderBy.complete = true;
        }
        return orderBy;
    }

    public enum SortType {
        ASC("asc"),
        DESC("desc");

        @Getter
        private String value;

        SortType(String value) {
            this.value = value;
        }
    }

    public static class Sort<T> {

        public enum FieldType {
            STRING,
            FIELD_FN
        }

        @Getter
        private SortType sortType;
        @Getter
        private String[] fields;
        @Getter
        private FieldFns.FieldFn<T, ?>[] fieldFns;
        @Getter
        private GroupBy.FieldType fieldType;

        private Sort() {
        }

        public Sort(SortType sortType, String fields) {
            this(sortType, StrUtil.split(fields, ",").toArray(new String[0]));
        }


        public Sort(SortType sortType, String... fields) {
            this.fields = fields;
            this.sortType = sortType;
            this.fieldType = GroupBy.FieldType.STRING;
        }

        public Sort(SortType sortType, FieldFns.FieldFn<T, ?>... fieldFns) {
            this.fieldFns = fieldFns;
            this.sortType = sortType;
            this.fieldType = GroupBy.FieldType.FIELD_FN;
        }

        public Sort(List<Object> fields, SortType sortType, GroupBy.FieldType fieldType) {
            this();
            this.sortType = sortType;
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

        private String configSort(BeanMeta<T> beanMeta, Map<String, Object> paraMap) {
            StringBuilder stringBuilder = new StringBuilder();
            switch (getFieldType()) {
                case STRING:
                    for (String field : getFields()) {
                        if (StrUtil.isNotEmpty(stringBuilder.toString())) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(field);
                        stringBuilder.append(" ");
                        stringBuilder.append(sortType.getValue());
                    }
                    break;
                case FIELD_FN:
                    for (FieldFns.FieldFn fieldFn : getFieldFns()) {
                        String column = BSColumnFinder.column(beanMeta.getBeanClass(), fieldFn);
                        if (StrUtil.isEmpty(column)) {
                            continue;
                        }
                        if (StrUtil.isNotEmpty(stringBuilder.toString())) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(column);
                        stringBuilder.append(" ");
                        stringBuilder.append(sortType.getValue());
                    }
                    break;
            }
            return stringBuilder.toString().replace("\n", "");
        }

    }

    private SqlSnippet configOrderBy(BeanMeta<T> beanMeta, Map<String, Object> paramMap) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (Sort<T> sort : getSorts()) {
                if (StrUtil.isNotEmpty(stringBuilder.toString())) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(sort.configSort(beanMeta, paramMap));
            }
            if (isCover()) {
                return new SqlSnippet(stringBuilder.toString().replace("\n", ""));
            } else {
                String origin = StrUtil.isNotEmpty(beanMeta.getGroupBy()) ? beanMeta.getGroupBy() + ", " : "";
                stringBuilder.insert(0, origin);
                return new SqlSnippet(stringBuilder.toString().replace("\n", ""));
            }
        } catch (UtilException e) {
            return null;
        }
    }




    @Getter
    private boolean cover = false;
    @Getter
    private List<Sort<T>> sorts = new ArrayList<>();
    @Getter
    SqlSnippet sql;
    @Getter
    boolean complete = false;

    public OrderBy() {}

    public OrderBy(Sort<T> ... sorts) {
        this.sorts.addAll(Arrays.asList(sorts));
    }

    public OrderBy(SortType type, FieldFns.FieldFn<T, ?> ... fieldFns) {
        final Sort<T> tSort = new Sort<>(type, fieldFns);
        this.sorts.add(tSort);
    }

    public OrderBy(boolean isCover) {
        this.cover = isCover;
    }

    public OrderBy<T> asc(FieldFns.FieldFn<T, ?>... fieldFns) {
        this.sorts.add(new Sort<>(SortType.ASC, fieldFns));
        return this;
    }

    public OrderBy<T> desc(FieldFns.FieldFn<T, ?>... fieldFns) {
        this.sorts.add(new Sort<>(SortType.DESC, fieldFns));
        return this;
    }

    public OrderBy<T> cover(boolean isConver) {
        this.cover = isConver;
        return this;
    }

}


