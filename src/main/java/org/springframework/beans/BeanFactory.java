package org.springframework.beans;

import org.springframework.beans.factory.BeansException;

/**
 * bean容器
 *
 * @author SnWalker
 */
public interface BeanFactory {

    /**
     * 获取bean
     *
     * @param name
     * @return
     */
    Object getBean(String name) throws BeansException;
}
