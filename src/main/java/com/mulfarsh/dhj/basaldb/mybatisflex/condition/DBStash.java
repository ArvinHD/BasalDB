package com.mulfarsh.dhj.basaldb.mybatisflex.condition;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.LambdaGetter;
import com.mybatisflex.core.util.LambdaUtil;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class DBStash {

    public static DBStash INSTANCE = new DBStash();

    public static DBStash INSTANCE_OF(DBColumn column) {
        return new DBStash(column);
    }

    public static DBStash INSTANCE_OF(DBColumn... columns) {
        return new DBStash(columns);
    }

    public static DBStash INSTANCE_OF(DBJoin join, DBColumn column) {
        DBStash stash = new DBStash(column);
        stash.join = join;
        return stash;
    }

    public static DBStash INSTANCE_OF(DBJoin join, DBColumn... columns) {
        DBStash stash = new DBStash(columns);
        stash.join = join;
        return stash;
    }

    public static DBStash INSTANCE_OF(LambdaGetter field, Object value, DBCondition condition) {
        DBColumn column = condition.generateColumn(field, value);
        return INSTANCE_OF(column);
    }

    public static DBStash INSTANCE_OF(LambdaGetter field, Object value, DBCondition condition, DBJoin join) {
        DBColumn column = condition.generateColumn(field, value);
        return INSTANCE_OF(join, column);
    }

    private enum ObjectCount {
        NONE,
        SINGLE,
        MULTI
    }

    @Setter
    private DBJoin join;

    private List<DBColumn> columns;
    private DBColumn column;
    private ObjectCount objectCount;

    public DBStash() {}

    public DBStash(DBColumn column) {
        this.column = column;
    }

    public DBStash(DBColumn... columns) {
        this.columns = Arrays.asList(columns);
    }

    public void and(QueryWrapper wrapper) {
        exec(wrapper, DBJoin.AND);
    }

    public void or(QueryWrapper wrapper) {
        exec(wrapper, DBJoin.OR);
    }

    public void bracketedOr(QueryWrapper wrapper) {
        exec(wrapper, DBJoin.BRACKETED_OR);
    }

    public void exec(QueryWrapper wrapper, DBJoin join) {
        if (join == null && this.join == null) {
            return;
        }
        if (join == null) {
            join = this.join;
        }
        switch (getObjectCount()) {
            case SINGLE:
                doJoin(wrapper, column, join);
                break;
            case MULTI:
                doJoin(wrapper, columns, join);
                break;
            case NONE:
                break;
        }
    }

    public void exec(QueryWrapper wrapper) {
        if (join == null) {
            return;
        }
        exec(wrapper, join);
    }

    public void empty() {
        column = null;
        columns = new ArrayList<>();
        objectCount = ObjectCount.NONE;
    }

    public ObjectCount getObjectCount() {
        if (column == null && CollUtil.isEmpty(columns)) {
            objectCount = ObjectCount.NONE;
        } else if (CollUtil.size(columns) > 1){
            objectCount = ObjectCount.MULTI;
        } else if (column != null && CollUtil.isEmpty(columns) ){
            objectCount = ObjectCount.SINGLE;
        } else {
            column = columns.get(0);
            objectCount = ObjectCount.SINGLE;
        }
        return objectCount;
    }

    public Optional<Object> getValue(String key) {
        switch (getObjectCount()) {
            case NONE:
                return Optional.empty();
            case SINGLE:
                if (key.equals(column.getFieldName())) {
                    return Optional.of(column.getValue());
                }
                return Optional.empty();
            case MULTI:
                final Map<String, Object> collect = columns.stream().collect(Collectors.toMap(DBColumn::getFieldName, DBColumn::getValue));
                if (collect.containsKey(key)) {
                    return Optional.of(collect.get(key));
                }
                return Optional.empty();
        }
        return null;
    }

    public Optional<Object> getValue(LambdaGetter key) {
        String keyName = LambdaUtil.getFieldName(key);
        return getValue(keyName);
    }

    public void doJoin(QueryWrapper wrapper, DBColumn column, DBJoin join) {
        switch (join) {
            case BRACKETED_OR:
            case OR:
                doOr(wrapper, column);
                break;
            case AND:
                doAnd(wrapper, column);
                break;
        }
    }

    public void doJoin(QueryWrapper wrapper, List<DBColumn> columns, DBJoin join) {
        switch (join) {
            case BRACKETED_OR:
                wrapper.and(q -> {
                    doOr(q, columns);
                });
                break;
            case AND:
                doAnd(wrapper, columns);
                break;
            case OR:
                doOr(wrapper, columns);
                break;
        }
    }

    private void doOr(QueryWrapper wrapper, List<DBColumn> columns) {
        for (DBColumn column: columns) {
            doOr(wrapper, column);
        }
    }

    private void doAnd(QueryWrapper wrapper, List<DBColumn> columns) {
        for (DBColumn column: columns) {
            doAnd(wrapper, column);
        }
    }

    private void doOr(QueryWrapper wrapper, DBColumn column) {
        wrapper.or(q -> {
            column.configCondition(q);
        });
    }

    private void doAnd(QueryWrapper wrapper, DBColumn column) {
        column.configCondition(wrapper);
    }
}
