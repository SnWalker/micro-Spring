package org.springframework.beans.factory.config;

/**
 * Bean 的定义信息类，存储 Bean 的元数据。
 *
 * <p>角色定位：【施工图纸】
 * <p>职责说明：保存了创建一个 Bean 所需的全部信息（如类对象、作用域等）。
 * 工人（实例化策略）拿到这张图纸，就知道该去造哪种型号的对象了。
 *
 * @author SnWalker
 */
public class BeanDefinition {

    /** Bean 的字节码类型（决定了要造一个什么样的对象） */
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
