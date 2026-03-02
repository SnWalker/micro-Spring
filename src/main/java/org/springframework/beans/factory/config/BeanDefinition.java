package org.springframework.beans.factory.config;

/**
 * BeanDefinition实例保存bean的信息，包括class类型、作用域（是否是单例）等，此处简化只包含class类型 （图纸，用于创建对象）
 *
 * @author SnWalker
 */
public class BeanDefinition {
    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }
    public Class getBeanClass() {
        return beanClass;
    }
    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
