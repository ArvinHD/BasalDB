package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.zhxu.bs.BeanMeta;
import cn.zhxu.bs.SearchParam;
import cn.zhxu.bs.SearchSql;
import cn.zhxu.bs.dialect.Dialect;
import cn.zhxu.bs.implement.DefaultSqlResolver;

public class ProxySqlResolver extends DefaultSqlResolver {

    public ProxySqlResolver() {
        super();
    }

    public ProxySqlResolver(Dialect dialect) {
        super(dialect);
    }

    @Override
    public <T> SearchSql<T> resolve(BeanMeta<T> beanMeta, SearchParam searchParam) {
        return super.resolve(beanMeta, searchParam);
    }
}
