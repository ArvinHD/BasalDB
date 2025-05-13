package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.hutool.core.util.RandomUtil;
import cn.zhxu.bs.FieldOp;
import cn.zhxu.bs.group.ExprParser;
import cn.zhxu.bs.util.AndBuilder;
import cn.zhxu.bs.util.FieldFns;
import cn.zhxu.bs.util.OrBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static cn.zhxu.bs.group.ExprParser.BRACKET_LEFT;
import static cn.zhxu.bs.group.ExprParser.BRACKET_RIGHT;
import static cn.zhxu.bs.util.MapBuilder.FIELD_PARAM;

public class ProxyAndBuilder extends AndBuilder implements BuilderProxy<ProxyAndBuilder> {

    private Integer randomLeft = 100;

    private String activingGroup;

    public ProxyAndBuilder(String parentExpr, String lastGroup) {
        super(parentExpr, lastGroup);
    }

    @Override
    public ProxyAndBuilder execOp(Class<? extends FieldOp> operator) {
        this.op(operator);
        return this;
    }

    @Override
    public ProxyAndBuilder execPage(Integer page, Integer size) {
        return this;
    }

    @Override
    public ProxyAndBuilder execLimit(long offset, int size) {
        return this;
    }

    @Override
    public ProxyAndBuilder execOr(Consumer<ProxyOrBuilder> condition) {
        Consumer<OrBuilder> proxyCondition = (proxyOrBuilder) -> {
            condition.accept((ProxyOrBuilder) proxyOrBuilder); // 将 ProxyOrBuilder 视为 OrBuilder
        };
        this.or(proxyCondition);
        return this;
    }

    @Override
    public ProxyAndBuilder execAnd(Consumer<ProxyAndBuilder> condition) {
        return this;
    }

    @Override
    public ProxyAndBuilder execGroup(String group) {
        this.group = Objects.requireNonNull(group);
        return this;
    }

    @Override
    public ProxyAndBuilder execGroupExpr(String gExpr) {
        super.setGroupExpr(gExpr);
        return this;
    }

    @Override
    protected ProxyAndBuilder withOr(Consumer<OrBuilder> condition, String parentExpr) {
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
    public <T> ProxyAndBuilder execField(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
        super.field(fieldFn, values);
        return this;
    }

    @Override
    protected String getGroupExpr() {
        return super.getGroupExpr();
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
