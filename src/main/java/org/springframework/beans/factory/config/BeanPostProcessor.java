package org.springframework.beans.factory.config;

import org.springframework.beans.factory.BeansException;

/**
 * 扩展点：修改 已实例化bean
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
