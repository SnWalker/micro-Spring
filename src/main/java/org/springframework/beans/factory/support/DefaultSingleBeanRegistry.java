package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例池 （成品仓库，存放单例对象，只负责存储和出货）
 */
public class DefaultSingleBeanRegistry implements SingletonBeanRegistry {
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 获取单例
     * @param beanName
     * @return
     */
    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    /**
     * 添加单例
     * @param beannName
     * @param singletonObject
     */
    public void addSingleton(String beannName, Object singletonObject) {
        singletonObjects.put(beannName, singletonObject);
    }
}
