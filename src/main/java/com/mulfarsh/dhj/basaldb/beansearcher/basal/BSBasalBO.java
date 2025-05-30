package com.mulfarsh.dhj.basaldb.beansearcher.basal;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Page;
import com.mulfarsh.dhj.basaldb.beansearcher.BSExecutor;
import com.mulfarsh.dhj.basaldb.beansearcher.extension.BSBeanTypeScanner;
import com.mulfarsh.dhj.basaldb.beansearcher.extension.Pager;
import com.mulfarsh.dhj.basaldb.beansearcher.proxy.ProxyMapBuilder;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public interface BSBasalBO<T extends BSBasalEntity> {

    Map<Class<?>, Class<? extends BSBasalEntity>> TYPE_CACHE = new ConcurrentHashMap<>();

    @PostConstruct
    default void init() {
        if (!TYPE_CACHE.containsKey(this.getClass())) {
            BSBasalBO.registerType(this.getClass(), this.getEntityType());
        }
    }

    default T queryOne() {
        return BSExecutor.queryOne(null, getEntityType());
    }

    default T queryOne(Map<String, Object> conditions) {
        if (CollUtil.isEmpty(conditions)) {
            return null;
        }
        return BSExecutor.queryOne(conditions, getEntityType());
    }

    default List<T> queryList() {
        return queryAll();
    }

    default List<T> queryList(Map<String, Object> conditions) {
        if (CollUtil.isEmpty(conditions)) {
            return new ArrayList<>();
        }
        final List<T> ts = BSExecutor.queryWith(conditions, getCachedType());
        if (CollUtil.isEmpty(ts)) {
            return new ArrayList<>();
        }
        return ts;
    }

    default Pager<T> page(Integer page, Integer size) {
        Map<String, Object> conditions = BaseBuilder().paging(page, size).build();
        return page(conditions);
    }

    default Pager<T> page(Map<String, Object> conditions) {
        if (CollUtil.isEmpty(conditions)) {
            return (Pager<T>) Pager.EMPTY;
        }
        return BSExecutor.pageWith(conditions, getCachedType());
    }

    default List<T> queryAll() {
        final List<T> ts = BSExecutor.queryAll(getCachedType());
        if (CollUtil.isEmpty(ts)) {
            return new ArrayList<>();
        }
        return ts;
    }

    default int queryCount(Map<String, Object> conditions) {
        if (CollUtil.isEmpty(conditions)) {
            return -1;
        }
        return BSExecutor.count(conditions, getCachedType());
    }

    default int queryCount() {
        return BSExecutor.count(null, getCachedType());
    }

    default ProxyMapBuilder BaseBuilder() {
        return ProxyMapBuilder.create();
    }

    default Class<T> getEntityType() {
        Class<T> tClass = (Class<T>) BSBeanTypeScanner.getActualType(this.getClass(), BSBasalBO.class, BSBasalEntity.class);
        return tClass;
    }

    default Class<T> getCachedType() {
        return (Class<T>) TYPE_CACHE.get(this.getClass());
    }

    // 子类可以直接调用，用于注册泛型类型
    static void registerType(Class<?> subclass, Class<? extends BSBasalEntity> typeClass) {
        TYPE_CACHE.putIfAbsent(subclass, typeClass);
    }


}
