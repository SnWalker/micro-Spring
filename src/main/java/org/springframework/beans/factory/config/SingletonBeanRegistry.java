package org.springframework.beans.factory.config;

/**
 * 单例 Bean 注册表接口。
 *
 * <p>角色定位：【成品仓库管理手册】
 * <p>设计思想：定义了单例对象的存取规范。
 * 它是实现“单例模式”的核心协议，确保整个容器生命周期内某个 Bean 只有一个实例。
 *
 * @author SnWalker
 */
public interface SingletonBeanRegistry {

    /**
     * 获取已经造好的单例成品。
     *
     * @param beanName Bean 的名称
     * @return 仓库里的成品对象，没有则返回 null
     */
    Object getSingleton(String beanName);
}
