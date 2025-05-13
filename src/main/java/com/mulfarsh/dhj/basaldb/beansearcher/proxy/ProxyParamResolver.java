package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.zhxu.bs.*;
import cn.zhxu.bs.implement.DefaultParamResolver;
import cn.zhxu.bs.param.FetchType;

import java.util.List;
import java.util.Map;

public class ProxyParamResolver extends DefaultParamResolver {

    public ProxyParamResolver() {
        super();
    }

    public ProxyParamResolver(List<FieldConvertor.ParamConvertor> convertors, List<ParamFilter> paramFilters) {
        super(convertors, paramFilters);
    }
        @Override
    public SearchParam resolve(BeanMeta<?> beanMeta, FetchType fetchType, Map<String, Object> paraMap) throws IllegalParamException {
        return super.resolve(beanMeta, fetchType, paraMap);
    }


}
