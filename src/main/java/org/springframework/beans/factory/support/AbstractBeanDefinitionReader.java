package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * 读取 BeanDefinition 信息的默认实现类 （三种来源：类路径、系统文件路径、URL资源路径）
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private final BeanDefinitionRegistry registry;
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    /**
     * 模版方法模式 + 缺省适配器模式
     *
     * 为什么不重写loadBeanDefinitions(Resource resource)）？
     * 抽象类不去实现 因场景而异、目前不知道怎么写 的方法，交给具体子类去实现一种场景的处理
     *
     * 体现Spring 大量使用 接口 -> 抽象类 -> 具体实现类 的三层架构设计原因：
     * 接口负责定义“要干什么”，抽象类负责把“大家一样的做法”写好，具体的子类负责实现“只有自己特殊的做法”，
     * 中间的抽象类实现 代码复用 ，无论未来你的子类是去读取 XML 文件（XmlBeanDefinitionReader）、还是读取 Properties 文件、甚至是读取注解，“遍历数组”这个动作永远是一模一样的
     */
    @Override
    public void loadBeanDefinitions(String[] locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }


    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
