package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的单例对象注册表实现。
 *
 * <p>角色定位：【成品仓库实体】
 * <p>设计思想：体现了<b>单一职责原则 (SRP)</b>。它唯一的任务就是管理单例池的存取，
 * 专门负责单例对象的“入库”与“出库”，确保整个容器生命周期内某个 Bean 只有一个实例。
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** 单例对象缓存池（成品仓库的货架，存着名字与对象的对应关系） */
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();


    @Override
    public Object getSingleton(String beanName) {
        // 从 Map 中直接获取已存在的对象实例（直接从货架上取成品）
        return singletonObjects.get(beanName);
    }

    /**
     * 将对象放入单例池。
     *
     * @param beanName Bean 名称
     * @param singletonObject 成品对象
     */
    public void addSingleton(String beanName, Object singletonObject) {
        // 将实例放入单例池（产品造好后，运入仓库货架）
        singletonObjects.put(beanName, singletonObject);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
