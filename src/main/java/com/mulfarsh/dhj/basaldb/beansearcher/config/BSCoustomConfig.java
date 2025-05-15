package com.mulfarsh.dhj.basaldb.beansearcher.config;

import cn.zhxu.bs.*;
import cn.zhxu.bs.boot.prop.BeanSearcherParams;
import cn.zhxu.bs.dialect.Dialect;
import cn.zhxu.bs.group.GroupPair;
import cn.zhxu.bs.group.GroupResolver;
import cn.zhxu.bs.implement.JoinParaSerializer;
import com.mulfarsh.dhj.basaldb.beansearcher.BSExecutor;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.BSFieldConvertor;
import com.mulfarsh.dhj.basaldb.beansearcher.operator.Distinct;
import com.mulfarsh.dhj.basaldb.beansearcher.operator.GroupBy;
import com.mulfarsh.dhj.basaldb.beansearcher.operator.OrderBy;
import com.mulfarsh.dhj.basaldb.beansearcher.proxy.ProxyBeanSearcher;
import com.mulfarsh.dhj.basaldb.beansearcher.proxy.ProxyParamResolver;
import com.mulfarsh.dhj.basaldb.beansearcher.proxy.ProxySqlResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
public class BSCoustomConfig {

    @Bean
    public BSExecutor bsExcutor() {
        return new BSExecutor();
    }

    @Bean
    @ConditionalOnMissingBean(ParamResolver.class)
    public ParamResolver paramResolver(PageExtractor pageExtractor,
                                       FieldOpPool fieldOpPool,
                                       List<ParamFilter> paramFilters,
                                       List<FieldConvertor.ParamConvertor> convertors,
                                       GroupResolver groupResolver,
                                       BeanSearcherParams config) {
        ProxyParamResolver paramResolver = new ProxyParamResolver(convertors, paramFilters);
        paramResolver.setPageExtractor(pageExtractor);
        paramResolver.setFieldOpPool(fieldOpPool);
        paramResolver.setGroupResolver(groupResolver);
        BeanSearcherParams.Group group = config.getGroup();
        paramResolver.getConfiguration()
                .gexprMerge(group.isMergeable())
                .groupSeparator(group.getSeparator())
                .gexpr(group.getExprName())
                .selectExclude(config.getSelectExclude())
                .onlySelect(config.getOnlySelect())
                .separator(config.getSeparator())
                .op(config.getOperatorKey())
                .ic(config.getIgnoreCaseKey())
                .orderBy(config.getOrderBy())
                .order(config.getOrder())
                .sort(config.getSort());
        return paramResolver;
    }

    @Bean
    @ConditionalOnMissingBean(SqlResolver.class)
    public SqlResolver sqlResolver(Dialect dialect, ObjectProvider<GroupPair.Resolver> groupPairResolver,
                                   ObjectProvider<JoinParaSerializer> joinParaSerializer) {
        ProxySqlResolver resolver = new ProxySqlResolver(dialect);
        ifAvailable(groupPairResolver, resolver::setGroupPairResolver);
        ifAvailable(joinParaSerializer, resolver::setJoinParaSerializer);
        return resolver;
    }


    @Bean
    @ConditionalOnMissingBean(BeanSearcher.class)
    public BeanSearcher beanSearcher(MetaResolver metaResolver,
                                     ParamResolver paramResolver,
                                     SqlResolver sqlResolver,
                                     SqlExecutor sqlExecutor,
                                     BeanReflector beanReflector,
                                     ObjectProvider<List<SqlInterceptor>> interceptors,
                                     ObjectProvider<List<ResultFilter>> processors,
                                     BeanSearcherParams config) {
        ProxyBeanSearcher searcher = new ProxyBeanSearcher();
        searcher.setMetaResolver(metaResolver);
        searcher.setParamResolver(paramResolver);
        searcher.setSqlResolver(sqlResolver);
        searcher.setSqlExecutor(sqlExecutor);
        searcher.setBeanReflector(beanReflector);
        searcher.setFailOnParamError(config.isFailOnError());
        ifAvailable(interceptors, searcher::setInterceptors);
        ifAvailable(processors, searcher::setResultFilters);
        return searcher;
    }

    private <T> void ifAvailable(ObjectProvider<T> provider, Consumer<T> consumer) {
        // 为了兼容 1.x 的 SpringBoot，最低兼容到 v1.4
        // 不直接使用 ObjectProvider.ifAvailable 方法
        T dependency = provider.getIfAvailable();
        if (dependency != null) {
            consumer.accept(dependency);
        }
    }

    @Bean
    public BSFieldConvertor bsFieldConvertor() {
        return new BSFieldConvertor();
    }

    @Bean
    public ParamFilter paramFilter() {
        return new ParamFilter() {
            @Override
            public <T> Map<String, Object> doFilter(BeanMeta<T> beanMeta, Map<String, Object> paraMap) throws IllegalParamException {
                System.out.println(beanMeta.getBeanClass().getName());
                if (!paraMap.containsKey(GroupBy.class.getName()) && !paraMap.containsKey(Distinct.class.getName())) {
                    return paraMap;
                }
                // beanMeta 是正在检索的实体类的元信息, paraMap 是当前的检索参数
                GroupBy groupBy = GroupBy.gropyBy(beanMeta, paraMap);
                Distinct distinct = Distinct.distinct(beanMeta, paraMap);
                OrderBy orderBy = OrderBy.orderBy(beanMeta, paraMap);
                return paraMap;
            }
        };
    }

}
