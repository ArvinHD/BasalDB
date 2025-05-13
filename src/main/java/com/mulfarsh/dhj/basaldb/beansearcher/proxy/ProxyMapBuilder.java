package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.hutool.core.util.RandomUtil;
import cn.zhxu.bs.FieldOp;
import cn.zhxu.bs.group.ExprParser;
import cn.zhxu.bs.util.FieldFns;
import cn.zhxu.bs.util.MapBuilder;
import cn.zhxu.bs.util.OrBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cn.zhxu.bs.group.ExprParser.BRACKET_LEFT;
import static cn.zhxu.bs.group.ExprParser.BRACKET_RIGHT;

public class ProxyMapBuilder extends MapBuilder implements BuilderProxy<ProxyMapBuilder> {

    private Integer randomLeft = 100;

    public static ProxyMapBuilder create() {
        return new ProxyMapBuilder(new HashMap<>());
    }

    public ProxyMapBuilder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public ProxyMapBuilder execOp(Class<? extends FieldOp> operator) {
        this.op(operator);
        return this;
    }

    @Override
    public ProxyMapBuilder execPage(Integer page, Integer size) {
        this.page(page, size);
        return this;
    }

    @Override
    public ProxyMapBuilder execLimit(long offset, int size) {
        this.limit(offset, size);
        return this;
    }

    @Override
    public ProxyMapBuilder execOr(Consumer<ProxyOrBuilder> condition) {
        Consumer<OrBuilder> proxyCondition = (proxyOrBuilder) -> {
            condition.accept((ProxyOrBuilder) proxyOrBuilder); // 将 ProxyOrBuilder 视为 OrBuilder
        };
        this.or(proxyCondition);
        return this;
    }

    @Override
    public ProxyMapBuilder execAnd(Consumer<ProxyAndBuilder> condition) {
        return this;
    }

    @Override
    public ProxyMapBuilder execGroup(String group) {
        this.group(group);
        return this;
    }

    @Override
    public ProxyMapBuilder execGroupExpr(String gExpr) {
        this.groupExpr(gExpr);
        return this;
    }

    @Override
    protected ProxyMapBuilder withOr(Consumer<OrBuilder> condition, String parentExpr) {
        ProxyOrBuilder builder = new ProxyOrBuilder(parentExpr, group);
        condition.accept(builder);
        String expr1 = builder.getGroupExpr();
        if (expr1 != null && !builder.map().isEmpty()) {
            String expr0 = getGroupExpr();
            if (expr0 != null) {
                setGroupExpr(BRACKET_LEFT + expr0 + BRACKET_RIGHT + ExprParser.AND_OP + BRACKET_LEFT + expr1 + BRACKET_RIGHT);
            } else {
                setGroupExpr(expr1);
            }
        }
        map.putAll(builder.map());
        return this;
    }

    @Override
    public <T> ProxyMapBuilder execField(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        super.field(fieldFn, values);
        return this;
    }

    @Override
    public String groupStr() {
        return group;
    }

    @Override
    public Map<String, Object> map() {
        return map;
    }

    @Override
    public <T> String fieldkey(FieldFns.FieldFn<T, ?> fieldFn) {
        return group + FIELD_PARAM + FieldFns.name(fieldFn);
    }

    @Override
    public String groupExpr() {
        return super.getGroupExpr();
    }

    @Override
    public Integer random() {
        Integer random = RandomUtil.randomInt(randomLeft, randomLeft + 10);
        randomLeft = random + 1;
        return random;
    }

}
