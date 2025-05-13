package com.mulfarsh.dhj.basaldb.beansearcher.config;

import cn.zhxu.bs.SearchSql;
import cn.zhxu.bs.SqlInterceptor;
import cn.zhxu.bs.param.FetchType;

import java.util.Map;

public class BSSqlInterceptor implements SqlInterceptor {
    @Override
    public <T> SearchSql<T> intercept(SearchSql<T> searchSql, Map<String, Object> paraMap, FetchType fetchType) {
        System.out.println(searchSql.getListSqlString());
//        System.out.println(JSONUtil.parseObj(paraMap).toStringPretty());
        return searchSql;
    }
}
