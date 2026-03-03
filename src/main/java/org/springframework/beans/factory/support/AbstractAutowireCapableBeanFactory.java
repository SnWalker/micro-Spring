package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 具备自动装配能力的抽象工厂。
 *
 * <p>角色定位：【全能型生产小组长】（指挥工人搬砖，预留装修接口）
 * <p>设计思想：使用了<b>策略模式 (Strategy Pattern)</b>。它不直接负责 new 对象，
 * 而是通过 InstantiationStrategy 接口（搬砖策略）在运行时动态选择最合适的方案。
 * 遵循了<b>单一职责原则 (SRP)</b>：组长负责控制“生产流程”，具体的“搬砖动作”交给策略类。
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    /** 实例化策略：负责具体的对象创建动作（真正的苦力活在这里） */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(beanName, beanDefinition);
    }

    /**
     * 执行具体的创建流程。
     */
    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            // 委派给专门的策略模式方法进行实例化（把造对象的脏活累活包给专业的打工人）
            bean = createBeanInstance(beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate [" + beanName + "]", e);
        }

        // 将创建好的成品/半成品加入单例缓存池（将成品运入成品仓库存档）
        addSingleton(beanName, bean);
        return bean;
    }

    /**
     * 实例化委派方法。
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition) throws BeansException {
        // 调用具体的实例化策略（如反射或 CGLIB）
        return this.getInstantiationStrategy().instantiate(beanDefinition);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
