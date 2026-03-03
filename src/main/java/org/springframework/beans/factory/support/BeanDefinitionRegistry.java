package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition 的注册中心接口。
 *
 * <p>角色定位：【档案员 / 图纸登记处】
 * <p>职责说明：定义了将“施工图纸”存入档案库的标准操作。
 * 只有登记在册的图纸，车间主任（AbstractBeanFactory）才能调取得出来。
 *
 * @author SnWalker
 */
public interface BeanDefinitionRegistry {

    /**
     * 将施工图纸登记到档案库中。
     *
     * @param beanName 图纸对应的唯一编号
     * @param beanDefinition 具体的施工细节图纸
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
