package org.springframework.beans.factory;

import java.util.HashMap;
import java.util.Map;

/**
 *
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
