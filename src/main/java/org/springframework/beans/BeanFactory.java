package org.springframework.beans;

import org.springframework.beans.factory.BeansException;

/**
 * Bean 容器的顶级接口。
 *
 * <p>角色定位：【工厂大管家 / 核心入口】
 * <p>设计思想：采用了<b>外观模式 (Facade Pattern)</b>，为复杂的子系统提供统一的访问接口。
 * 它定义了获取 Bean 的最基本契约（接口隔离原则 ISP），使用户只需关心“我要什么”，
 * 而无需关心“怎么造出来的”。
 *
 * @author SnWalker
 */
public interface BeanFactory {

    /**
     * 根据名称获取 Bean 实例。
     *
     * @param name Bean 的唯一标识名称
     * @return 对应的 Bean 实例
     * @throws BeansException 如果获取过程中发生异常（如找不到、实例化失败等）
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据名称和类型来查找bean
     */
    // 重载getBean()，引入泛型，避免手动强制类型转换，编译器自动检查类型转换是否匹配，减少运行时ClassCastException
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
