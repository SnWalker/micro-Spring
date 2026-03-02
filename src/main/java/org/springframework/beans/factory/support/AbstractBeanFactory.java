package org.springframework.beans.factory.support;

import org.springframework.beans.BeanFactory;
import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 车间主任（只定规矩，不干活）。实现了 getBean() 的核心骨架（规矩）：先去成品仓库找(getSingleton) -> 找不到就去拿图纸 (getBeanDefinition) -> 拿到图纸就去造对象 (createBean)。
 *
 * 模版方法设计模式：BeanFactory(接口) -> DefaultListableBeanFactory(集大成者，创建对象能力 + 管理图纸能力)
 */
public abstract class AbstractBeanFactory extends DefaultSingleBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String name) {
        Object bean = this.getSingleton(name);
        if (bean != null) {
            return bean;
        }
        // 单例池没有，先获取bean信息，再创建一个
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name, beanDefinition);
    }

    protected abstract Object createBean(String name, BeanDefinition beanDefinition) throws BeansException;

    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;
}
