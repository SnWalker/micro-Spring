package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的 Bean 工厂实现，容器的最终实体。
 *
 * <p>角色定位：【全能型工厂实体】（既管图纸又管生产，是真正的核心）
 * <p>设计思想：遵循了<b>依赖倒置原则 (DIP)</b>。高层逻辑（AbstractBeanFactory）依赖抽象获取图纸，
 * 而本类作为具体实现，利用聚合的 Map 维护了一份真实的图纸档案库（beanDefinitionMap）。
 * 它是 Spring 设计中“职责融合”的典型代表。
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    /** 容器内的 Bean 定义注册表（工厂内部的图纸柜） */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        // 将定义的 Bean 信息存入 Map 中（把施工图纸登记到档案柜中）
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean definition found for name: " + beanName);
        }
        return beanDefinition;
    }
}
