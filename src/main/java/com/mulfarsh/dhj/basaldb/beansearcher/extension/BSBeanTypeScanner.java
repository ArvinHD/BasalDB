package com.mulfarsh.dhj.basaldb.beansearcher.extension;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;

public class BSBeanTypeScanner {

    public static Class<?> findInInterface(Class<?> clazz, Class<?> targetClass, Class<?> type, Integer index) {
        if (index == null && type == null) {
            return null;
        }
        if (clazz.equals(Object.class)) {
            return null;
        }
        final Class<?>[] interfaces = clazz.getInterfaces();
        Class<?> resultType = null;
        for (Class<?> anInterface : interfaces) {
            if (!targetClass.isAssignableFrom(anInterface)) {
                continue;
            }
            if (index != null) {
                resultType = findByIndex(anInterface, TypeUtil.getTypeArguments(anInterface), index);
            } else if (type != null) {
                resultType = findByType(anInterface, TypeUtil.getTypeArguments(anInterface), type);
            }
            if (resultType == null) {
                resultType = findInClass(anInterface.getSuperclass(), type, index);
            }
            if (resultType != null) {
                break;
            }
        }
        if (resultType == null) {
            resultType = findInInterface(clazz.getSuperclass(), targetClass, type, index);
        }
        return resultType;
    }

    public static Class<?> findInClass(Class<?> clazz, Class<?> type, Integer index) {
        if (index == null && type == null) {
            return null;
        }
        if (clazz.equals(Object.class)) {
            return null;
        }
        final Type[] typeArguments = TypeUtil.getTypeArguments(clazz);
        Class<?> resultType = null;
        if (ArrayUtil.isEmpty(typeArguments)) {
            resultType = findInClass(clazz.getSuperclass(), type, index);
        }
        if (index != null) {
            resultType = findByIndex(clazz, typeArguments, index);
        } else if (type != null) {
            resultType = findByType(clazz, typeArguments, type);
        }
        if (resultType == null) {
            resultType = findInClass(clazz.getSuperclass(), type, index);
        }
        return resultType;

    }

    public static Class<?> findByIndex(Class<?> clazz, Type[] typeArguments, Integer index) {
        if (ArrayUtil.isEmpty(typeArguments) || typeArguments.length >= index) {
            return null;
        }
        return (Class<?>) typeArguments[index];
    }

    public static Class<?> findByType(Class<?> clazz, Type []typeArguments, Class<?> type) {
        for (Type typeArgument : typeArguments) {
            if (type.isAssignableFrom((Class<?>) typeArgument)) {
                return (Class<?>) typeArgument;
            }
        }
        return null;
    }

    /**
     * 获取目标接口的泛型实际类型。
     *
     * @param clazz           目标类，从该类开始向上查找
     * @param targetClass 目标接口，例如 BSBasalBO.class
     * @return 泛型实际类型；如果未找到，则返回 null
     */
    public static Class<?> getActualType(Class<?> clazz, Class<?> targetClass, Class<?> type) {
        if (stopFind(clazz, targetClass)) return null; // 停止条件：达到根类或没有父类了

        if (ClassUtil.isInterface(targetClass)) {
            // 查找接口的泛型
            return findInInterface(clazz, targetClass, type, null);
        } else {
            // 查找类的泛型
            return findInClass(clazz, type, null);
        }

    }

    private static boolean stopFind(Class<?> clazz, Class<?> targetClass) {
        if (clazz == null || clazz == Object.class) {
            return true;
        }

        // 并不继承目标类，停止
        if (!targetClass.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public static Class<?> getActualType(Class<?> clazz, Class<?> targetClass, Integer index) {
        if (stopFind(clazz, targetClass)) return null;

        if (ClassUtil.isInterface(targetClass)) {
            // 查找接口的泛型
            return findInInterface(clazz, targetClass, null, index);
        } else {
            // 查找类的泛型
            return findInClass(clazz, null, index);
        }

    }

}