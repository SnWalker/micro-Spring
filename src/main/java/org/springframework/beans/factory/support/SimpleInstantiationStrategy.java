package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 简单的 Bean 实例化策略。
 *
 * <p>角色定位：【原生打工人】
 * <p>职责说明：使用 JDK 原生的反射机制（Constructor）来创建对象。
 * 这是最基础的实现方案，要求 Bean 必须提供一个可访问的无参构造函数。
 *
 * @author SnWalker
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    /**
     * 基于 JDK 反射的无参构造函数实例化。
     *
     * @param beanDefinition Bean 定义
     * @return 实例化对象
     * @throws BeansException 实例化失败异常
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            // 获取无参构造器并创建实例
            Constructor<?> constructor = beanClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate [" + beanClass.getName() + "] via reflection", e);
        }
    }
}
