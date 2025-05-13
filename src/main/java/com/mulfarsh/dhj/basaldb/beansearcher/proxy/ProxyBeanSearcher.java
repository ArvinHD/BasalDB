package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.zhxu.bs.SqlExecutor;
import cn.zhxu.bs.implement.DefaultBeanSearcher;

public class ProxyBeanSearcher extends DefaultBeanSearcher {

    public ProxyBeanSearcher(SqlExecutor sqlExecutor) {
        super(sqlExecutor);
    }

    public ProxyBeanSearcher() {
        super();
    }

}