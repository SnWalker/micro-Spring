package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的 Bean 工厂基类。
 *
 * <p>角色定位：【车间主任】（负责编排生产工序，具体干活交给下级）
 * <p>设计思想：典型的<b>模板方法模式 (Template Method Pattern)</b>。它定义了 getBean 的核心执行算法：
 * 1. 尝试从成品仓库直接拿成品 (getSingleton)；
 * 2. 仓库没有，去档案室调取图纸 (getBeanDefinition)；
 * 3. 下达指令：开始造这个 Bean (createBean)。
 * 遵循了<b>开闭原则 (OCP)</b>，核心流程不可变，但具体步骤（如何拿图纸、如何造对象）由子类扩展。
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) {
        // 首先尝试从成品缓存中获取已存在的单例（先去仓库看看有没有现成的成品）
        Object bean = this.getSingleton(name);
        if (bean != null) {
            return bean;
        }

        // 如果缓存没有，则调取对应的 Bean 定义信息（去档案室调取施工图纸）
        BeanDefinition beanDefinition = getBeanDefinition(name);

        // 根据图纸信息创建新的 Bean 实例（交给车间小组长开始生产）
        return createBean(name, beanDefinition);
    }

    /**
     * 抽象方法：创建 Bean 实例。由 AbstractAutowireCapableBeanFactory 实现。
     */
    protected abstract Object createBean(String name, BeanDefinition beanDefinition) throws BeansException;

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return ((T) getBean(name));
    }
    /**
     * 抽象方法：获取 Bean 定义信息。由 DefaultListableBeanFactory 实现。
     */
    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 有则覆盖
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
}
