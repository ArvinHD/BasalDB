package com.mulfarsh.dhj.basaldb.beansearcher;

import cn.hutool.core.collection.CollUtil;
import cn.zhxu.bs.BeanSearcher;
import cn.zhxu.bs.SearchResult;
import cn.zhxu.bs.util.MapBuilder;
import com.mulfarsh.dhj.basaldb.beansearcher.basal.BSBasalEntity;
import com.mulfarsh.dhj.basaldb.beansearcher.extension.Pager;
import com.mulfarsh.dhj.basaldb.beansearcher.proxy.ProxyMapBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BSExecutor {

    private final static String PAGING_PARAM_KEY = "cn.zhxu.bs.param.Paging";

    public static <T extends BSBasalEntity> T queryOne(Map<String, Object> condition, Class<T> tClass) {
        if (CollUtil.isEmpty(condition)) {
            return INSTANCE.beanSearcher.searchFirst(tClass);
        }
        return INSTANCE.beanSearcher.searchFirst(tClass, condition);
    }

    public static <T extends BSBasalEntity> List<T> queryAll(Class<T> tClass) {
        List <T> ts = INSTANCE.beanSearcher.searchAll(tClass);
        if (CollUtil.isEmpty(ts)) {
            return new ArrayList<>();
        }
        return ts;
    }

    public static <T extends BSBasalEntity> List<T> queryWith(Map<String, Object> condition, Class<T> tClass) {
        final List<T> ts = INSTANCE.beanSearcher.searchAll(tClass, condition);
        if (CollUtil.isEmpty(ts)) {
            return new ArrayList<>();
        }
        return ts;
    }

    public static <T extends BSBasalEntity> Pager<T> pageWith(Map<String, Object> condition, Class<T> tClass) {
        MapBuilder.Page pageParam = (MapBuilder.Page) condition.getOrDefault(PAGING_PARAM_KEY, null);
        if (pageParam  != null) {
            SearchResult<T> search = INSTANCE.beanSearcher.search(tClass, condition);
            return new Pager<>(search, pageParam);
        } else {
            final List<T> ts = INSTANCE.beanSearcher.searchAll(tClass, condition);
            return new Pager<>(ts);
        }
    }

    public static <T extends BSBasalEntity> Pager<T> pageWith(Map<String, Object> condition, Integer page, Integer size, Class<T> tClass) {
        ProxyMapBuilder builder = new ProxyMapBuilder(condition);
        condition = builder.paging(page, size).build();
        final SearchResult<T> search = INSTANCE.beanSearcher.search(tClass, condition);
        if (CollUtil.isEmpty(search.getDataList())) {
            return new Pager<>();
        }
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = search.getDataList().size();
        }
        final List<T> ts = search.getDataList();
        return new Pager<>(ts, (long) page, (long) size, (long) search.getTotalCount());
    }

    public static <T extends BSBasalEntity> List<T> query(String sql, Class<T> tClass) {
        final SearchResult<T> search = INSTANCE.beanSearcher.search(tClass, sql);
        return search.getDataList();
    }

    public static <T extends Object> List<T> queryBySql(String sql, Class<T> tClass) {
        final SearchResult<T> search = INSTANCE.beanSearcher.search(tClass, sql);
        if (CollUtil.isEmpty(search.getDataList())) {
            return new ArrayList<>();
        }
        return search.getDataList();
    }

    private static BSExecutor INSTANCE = new BSExecutor();

    @Resource
    private BeanSearcher beanSearcher;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }
}
