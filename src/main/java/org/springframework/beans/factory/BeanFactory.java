package org.springframework.beans.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 简易Bean对象的管理容器BeanFactory。
 * <p>
 * 目前仅实现了单例对象的注册与获取功能。
 * 核心数据结构为 {@code Map<String, Object>}，用于缓存 Bean 实例。
 *
 * @author SnWalker
 */
public class BeanFactory {
    private Map<String, Object> beanMap = new HashMap<>();

    public void registerBean(String beanName, Object bean) {
        beanMap.put(beanName, bean);
    }
    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }
}
