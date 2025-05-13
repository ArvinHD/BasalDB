package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.zhxu.bs.FieldOp;
import cn.zhxu.bs.operator.*;
import cn.zhxu.bs.util.AndBuilder;
import cn.zhxu.bs.util.FieldFns;
import cn.zhxu.bs.util.OrBuilder;
import com.mulfarsh.dhj.basaldb.beansearcher.extension.BSBuilderField;
import com.mulfarsh.dhj.basaldb.beansearcher.operator.Distinct;
import com.mulfarsh.dhj.basaldb.beansearcher.operator.GroupBy;
import com.mulfarsh.dhj.basaldb.beansearcher.operator.OrderBy;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

interface BuilderProxy<B extends BuilderProxy> {

    default <T> B or(BSBuilderField<T>... fields) {
        if (ObjUtil.isEmpty(fields)) {
            return (B) this;
        }
        if (this instanceof OrBuilder) {
            return (B) this;
        }
        this.execOr(o -> {
            for (BSBuilderField<T> field : fields) {
                if (ObjUtil.isNotEmpty(field.getValues())) {
                    executeOr(o, field.getFieldOp(), field.getFieldFn(), field.getValues());
                }
            }
        });
        return (B) this;
    }

    default <T> B or(Class<? extends FieldOp> fieldOp, FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        if (ObjUtil.isEmpty(values)) {
            return (B) this;
        }
        if (this instanceof OrBuilder) {
            return (B) this;
        }
        this.execOr(o -> {
            for (Object value : values) {
                if (ObjUtil.isNotEmpty(value)) {
                    executeOr(o, fieldOp, fieldFn, value);
                }
            }
        });
        return (B) this;
    }

    default B doOr(Consumer<ProxyOrBuilder> condition) {
        this.execOr(condition);
        return (B) this;
    }

    default B doAnd(Consumer<ProxyAndBuilder> condition) {
        this.execAnd(condition);
        return (B) this;
    }

    default <T> B equal(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(Equal.class, fieldFn, value);
        return (B) this;
    }

    default <T> B orEqual(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        or(Equal.class, fieldFn, values);
        return (B) this;
    }

    default <T> B notEqual(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(NotEqual.class, fieldFn, value);
        return (B) this;
    }

    default <T> B orNotEqual(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        or(NotEqual.class, fieldFn, value);
        return (B) this;
    }

    default <T> B grandThen(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(GreaterThan.class, fieldFn, value);
        return (B) this;
    }

    default <T> B lessThen(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(LessThan.class, fieldFn, value);
        return (B) this;
    }

    default <T> B grandEqual(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(GreaterEqual.class, fieldFn, value);
        return (B) this;
    }

    default <T> B lessEqual(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(LessEqual.class, fieldFn, value);
        return (B) this;
    }

    default <T> B in(FieldFns.FieldFn<T, ?> fieldFn, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return (B) this;
        }
        execute(InList.class, fieldFn, collection.toArray());
        return (B) this;
    }

    default <T> B orIn(FieldFns.FieldFn<T, ?> fieldFn, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return (B) this;
        }
        collection = collection.stream().filter(item -> ObjUtil.isNotEmpty(item)).collect(Collectors.toList());
        Object[] objects = collection.toArray();
        or(InList.class, fieldFn, objects);
        return (B) this;
    }

    default <T> B orInMulti(FieldFns.FieldFn<T, ?> fieldFn, Collection<Collection<?>> collection) {
        if (CollUtil.isEmpty(collection)) {
            return (B) this;
        }
        collection = collection.stream().filter(coll -> CollUtil.isNotEmpty(coll)).collect(Collectors.toList());
        Object[][] objects = toDoubleArray(collection);
        or(InList.class, fieldFn, objects);
        return (B) this;
    }

    default <T> B in(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        execute(InList.class, fieldFn, values);
        return (B) this;
    }

    default <T> B orIn(FieldFns.FieldFn<T, ?> fieldFn, Object[]... values) {
        or(InList.class, fieldFn, values);
        return (B) this;
    }


    default <T> B notIn(FieldFns.FieldFn<T, ?> fieldFn, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return (B) this;
        }
        execute(NotIn.class, fieldFn, collection.toArray());
        return (B) this;
    }

    default <T> B orNotIn(FieldFns.FieldFn<T, ?> fieldFn, Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            return (B) this;
        }
        Object[] objects = collection.toArray();
        or(NotIn.class, fieldFn, objects);
        return (B) this;
    }

    default <T> B orNotInMulti(FieldFns.FieldFn<T, ?> fieldFn, Collection<Collection<?>> collection) {
        if (CollUtil.isEmpty(collection)) {
            return (B) this;
        }
        Object[][] objects = toDoubleArray(collection);
        or(NotIn.class, fieldFn, objects);
        return (B) this;
    }

    default <T> B notIn(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        execute(NotIn.class, fieldFn, values);
        return (B) this;
    }

    default <T> B orNotIn(FieldFns.FieldFn<T, ?> fieldFn, Object[]... values) {
        execute(NotIn.class, fieldFn, values);
        return (B) this;
    }

    default <T> B range(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        execute(Between.class, fieldFn, left, right);
        return (B) this;
    }

    default <T> B rangeExclude(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        grandThen(fieldFn, left);
        lessThen(fieldFn, right);
        return (B) this;
    }


    default <T> B rangeLeft(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        grandEqual(fieldFn, left);
        lessThen(fieldFn, right);
        return (B) this;
    }

    default <T> B rangeRight(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        grandThen(fieldFn, left);
        lessEqual(fieldFn, right);
        return (B) this;
    }

    default <T> B without(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        execute(NotBetween.class, fieldFn, left, right);
        return (B) this;
    }

    default <T> B withoutInclude(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        execute(LessEqual.class, fieldFn, left);
        execute(GreaterEqual.class, fieldFn, right);
        return (B) this;
    }

    default <T> B withoutLeft(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        execute(LessThan.class, fieldFn, left);
        execute(GreaterEqual.class, fieldFn, right);
        return (B) this;
    }

    default <T> B withoutRight(FieldFns.FieldFn<T, ?> fieldFn, Object left, Object right) {
        execute(LessEqual.class, fieldFn, left);
        execute(GreaterThan.class, fieldFn, right);
        ;
        return (B) this;
    }

    default <T> B like(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(Contain.class, fieldFn, value);
        return (B) this;
    }

    default <T> B likeWithout(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        like(fieldFn, value);
        notEqual(fieldFn, value);
        return (B) this;
    }

    default <T> B leftLike(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(StartWith.class, fieldFn, value);
        return (B) this;
    }

    default <T> B leftLikeWithout(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        leftLike(fieldFn, value);
        notEqual(fieldFn, value);
        return (B) this;
    }

    default <T> B rightLike(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(EndWith.class, fieldFn, value);
        return (B) this;
    }

    default <T> B rightLikeWithout(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        rightLike(fieldFn, value);
        notEqual(fieldFn, value);
        return (B) this;
    }

    default <T> B orLike(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        execute(OrLike.class, fieldFn, values);
        return (B) this;
    }

    default <T> B notLike(FieldFns.FieldFn<T, ?> fieldFn, Object value) {
        execute(NotLike.class, fieldFn, value);
        return (B) this;
    }

    default <T> B isNull(FieldFns.FieldFn<T, ?> fieldFn) {
        execute(IsNull.class, fieldFn);
        return (B) this;
    }

    default <T> B notNull(FieldFns.FieldFn<T, ?> fieldFn) {
        execute(NotNull.class, fieldFn);
        return (B) this;
    }

    default <T> B empty(FieldFns.FieldFn<T, ?> fieldFn) {
        execute(Empty.class, fieldFn);
        return (B) this;
    }

    default <T> B notEmpty(FieldFns.FieldFn<T, ?> fieldFn) {
        execute(NotEmpty.class, fieldFn);
        return (B) this;
    }

    default <T> B orderBy(OrderBy<T> orderBy) {
        map().put(OrderBy.ORDER_BY_KEY, orderBy);
        return (B) this;
    }

    default <T> B asc(FieldFns.FieldFn<T, ?>... fieldFns) {
        OrderBy<T> orderBy = (OrderBy<T>) map().getOrDefault(OrderBy.ORDER_BY_KEY, new OrderBy<T>());
        orderBy.asc(fieldFns);
        map().put(OrderBy.ORDER_BY_KEY, orderBy);
        return (B) this;
    }

    default <T> B desc(FieldFns.FieldFn<T, ?>... fieldFns) {
        OrderBy<T> orderBy = (OrderBy<T>) map().getOrDefault(OrderBy.ORDER_BY_KEY, new OrderBy<T>());
        orderBy.desc(fieldFns);
        map().put(OrderBy.ORDER_BY_KEY, orderBy);
        return (B) this;
    }

    default <T> B coverOrderBy() {
        if (map().containsKey(OrderBy.ORDER_BY_KEY)) {
            OrderBy<T> orderBy = (OrderBy<T>) map().get(OrderBy.ORDER_BY_KEY);
            orderBy.cover(true);
        }
        return (B) this;
    }

    default B distinct() {
        return distinct(true);
    }

    default B distinct(boolean distinct) {
        map().put(Distinct.DISTINCT_BY_KEY, new Distinct(distinct));
        return (B) this;
    }

    default <T> B groupBy(GroupBy<T> groupBy) {
        map().put(GroupBy.GROUP_BY_KEY, groupBy);
        return (B) this;
    }

    default <T> B groupBy(FieldFns.FieldFn<T, ?>... fieldFns) {
        map().put(GroupBy.GROUP_BY_KEY, new GroupBy<>(fieldFns));
        return (B) this;
    }

    default B first() {
        this.execLimit(0, 1);
        return (B) this;
    }

    default B grouping(String group) {
        this.execGroup(group);
        return (B) this;
    }

    default B groupingExpr(String gExpr) {
        this.execGroupExpr(gExpr);
        return (B) this;
    }

    default B paging(Integer page, Integer size) {
        if (page == null || size == null || page < 1 || size < 1) {
            return (B) this;
        }
        return this.execPage(page - 1, size);
    }

    default <T> void execute(Class<? extends FieldOp> operator, FieldFns.FieldFn<T, ?> fieldFn) {
        this.execField(fieldFn).execOp(operator);
    }

    default <T> void execute(Class<? extends FieldOp> operator, FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        if (ObjUtil.isEmpty(values)) {
            return;
        }
//        this.execField(fieldFn, values).execOp(operator);
        repetitiveField(operator, fieldFn, values);
    }

    default <T> void executeOr(OrBuilder orBuilder, Class<? extends FieldOp> operator, FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        if (ObjUtil.isEmpty(values)) {
            return;
        }
        orBuilder.field(fieldFn, values).op(operator);
    }

    default <T> void executeAnd(AndBuilder andBuilder, Class<? extends FieldOp> operator, FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        if (ObjUtil.isEmpty(values)) {
            return;
        }
        andBuilder.field(fieldFn, values).op(operator);
    }

    default Object[][] toDoubleArray(Collection<Collection<?>> collection) {
        collection = collection.stream().filter(coll -> CollUtil.isNotEmpty(coll)).collect(Collectors.toList());
        Object[][] objects = new Object[collection.size()][];
        final Iterator<Collection<?>> iterator = collection.iterator();
        for (int i = 0; i < collection.size(); i++) {
            Collection<?> ts = iterator.next();
            if (CollUtil.isNotEmpty(ts)) {
                objects[i] = ts.toArray();
            }
        }
        if (ObjUtil.isEmpty(objects)) {
            return null;
        }
        return objects;
    }

    default <T> void repetitiveField(Class<? extends FieldOp> operator,
                                     FieldFns.FieldFn<T, ?> fieldFn,
                                     Object... values) {
        if (map().keySet().contains(fieldkey(fieldFn))) {
            String group = groupStr();
            String element = group.equals("$") ? "#" + "_" + random() : group + "_" + random();
            grouping(element);
            this.execField(fieldFn, values).execOp(operator);
            StringBuilder groupExprBuilder = null;
            if (StrUtil.isEmpty(groupExpr()) || groupExpr().contains("$")) {
                groupExprBuilder = new StringBuilder();
            } else {
                groupExprBuilder = new StringBuilder(groupExpr()).append("&");
            }
            groupingExpr(groupExprBuilder.append(element).toString());
            grouping(group);
        } else {
            this.execField(fieldFn, values).execOp(operator);
        }
    }

    <T> B execField(FieldFns.FieldFn<T, ?> fieldFn, Object... values);

    B execOp(Class<? extends FieldOp> operator);

    B execLimit(long offset, int size);

    B execOr(Consumer<ProxyOrBuilder> condition);

    B execAnd(Consumer<ProxyAndBuilder> condition);

    B execGroup(String group);

    B execGroupExpr(String gExpr);

    B execPage(Integer page, Integer size);

    Map<String, Object> map();

    <T> String fieldkey(FieldFns.FieldFn<T, ?> fieldFn);

    String groupStr();

    String groupExpr();

    Integer random();
}
