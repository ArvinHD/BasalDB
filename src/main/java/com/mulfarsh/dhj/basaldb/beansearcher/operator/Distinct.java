package com.mulfarsh.dhj.basaldb.beansearcher.operator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.BeanMeta;
import lombok.Getter;

import java.util.Map;

public class Distinct {

    public static final String DISTINCT_BY_KEY = Distinct.class.getName();
    private static final String DISTINCT_BY_FIELD = "distinct";

    public static Distinct distinct(BeanMeta<?> beanMeta, Map<String, Object> paramMap) {
        if (!paramMap.containsKey(DISTINCT_BY_KEY)) {
            return null;
        }
        Distinct distinct = (Distinct) paramMap.get(DISTINCT_BY_KEY);
        paramMap.remove(DISTINCT_BY_KEY);
        if (StrUtil.isNotEmpty(beanMeta.getGroupBy()) || CollUtil.isNotEmpty(beanMeta.getGroupBySqlParas())) {
            distinct.sql = false;
            ReflectUtil.setFieldValue(beanMeta, DISTINCT_BY_FIELD, false);
            return distinct;
        }
        ReflectUtil.setFieldValue(beanMeta, DISTINCT_BY_FIELD, distinct.isDistinct());
        distinct.sql = distinct.isDistinct();
        return distinct;
    }

    @Getter
    private boolean distinct = true;

    @Getter
    private boolean sql = false;

    public Distinct() {}

    public Distinct(boolean distinct) { this.distinct = distinct; }

}
