package org.springframework.beans.factory.config;

import org.springframework.beans.PropertyValues;

/**
 * Bean 的定义信息类，存储 Bean 的元数据（class类型、方法构造参数、bean属性、作用域scope等）。此处，简化为只包含class类型、bean属性.
 *
 * <p>角色定位：【施工图纸】
 * <p>职责说明：保存了创建一个 Bean 所需的全部信息（如类对象、作用域等）。
 * 工人（实例化策略）拿到这张图纸，就知道该去造哪种型号的对象了。
 *
 * @author SnWalker
 */
public class BeanDefinition {

    /**
     * Bean 的字节码类型（决定了要造一个什么样的对象）
     */
    private Class beanClass;

    private PropertyValues propertyValues;

    public BeanDefinition(Class beanClass) {
        // 构造函数重载调用：调用同一个类中的另一个构造函数
        this(beanClass, null);
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
