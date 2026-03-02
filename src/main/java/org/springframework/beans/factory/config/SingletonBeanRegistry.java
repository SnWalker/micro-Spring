package org.springframework.beans.factory.config;

/**
 * 单例bean注册表
 *
 * @author SnWalker
 */
public interface SingletonBeanRegistry {
    /**
     * 获取单例bean
     *
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);
}
