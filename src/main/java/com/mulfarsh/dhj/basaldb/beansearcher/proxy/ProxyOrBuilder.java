package com.mulfarsh.dhj.basaldb.beansearcher.proxy;

import cn.hutool.core.util.RandomUtil;
import cn.zhxu.bs.FieldOp;
import cn.zhxu.bs.util.AndBuilder;
import cn.zhxu.bs.util.FieldFns;
import cn.zhxu.bs.util.OrBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static cn.zhxu.bs.group.ExprParser.*;
import static cn.zhxu.bs.util.MapBuilder.FIELD_PARAM;

public class ProxyOrBuilder extends OrBuilder implements BuilderProxy<ProxyOrBuilder> {

    private Integer randomLeft = 100;

    private String cachedParentExpr;

    public ProxyOrBuilder(String parentExpr, String lastGroup) {
        super(parentExpr, lastGroup);
        this.cachedParentExpr = parentExpr;
    }

    @Override
    public ProxyOrBuilder execOp(Class<? extends FieldOp> operator) {
        this.op(operator);
        return this;
    }

    @Override
    public ProxyOrBuilder execPage(Integer page, Integer size) {
        return this;
    }

    @Override
    public ProxyOrBuilder execLimit(long offset, int size) {
        return this;
    }

    @Override
    public ProxyOrBuilder execOr(Consumer<ProxyOrBuilder> condition) {
        return this;
    }

    @Override
    public ProxyOrBuilder execAnd(Consumer<ProxyAndBuilder> condition) {
        Consumer<AndBuilder> proxyCondition = (proxyOrBuilder) -> {
            condition.accept((ProxyAndBuilder) proxyOrBuilder); // 将 ProxyOrBuilder 视为 OrBuilder
        };
        this.and(proxyCondition);
        return this;
    }

    @Override
    public ProxyOrBuilder execGroup(String group) {
        this.group = Objects.requireNonNull(group);
        return this;
    }

    @Override
    public ProxyOrBuilder execGroupExpr(String gExpr) {
        super.setGroupExpr(gExpr);
        return this;
    }

    @Override
    protected String getGroupExpr() {
        String groupExpr = super.getGroupExpr();
        System.out.println(groupExpr);
        return groupExpr;
    }

    @Override
    public ProxyOrBuilder and(Consumer<AndBuilder> condition) {
        ProxyAndBuilder builder = new ProxyAndBuilder(cachedParentExpr + getGroupExpr(), group);
        condition.accept(builder);
        String expr1 = builder.getGroupExpr();
        if (expr1 != null) {
            String expr0 = getGroupExpr();
            if (expr0 != null) {
                setGroupExpr(BRACKET_LEFT + expr0 + BRACKET_RIGHT + OR_OP + BRACKET_LEFT + expr1 + BRACKET_RIGHT);
            } else {
                setGroupExpr(BRACKET_LEFT + expr1 + BRACKET_RIGHT);
            }
        }
        map.putAll(builder.map());
        return this;
    }

    @Override
    public <T> ProxyOrBuilder execField(FieldFns.FieldFn<T, ?> fieldFn, Object... values) {
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
