package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Bean 实例化策略接口。
 *
 * <p>角色定位：【搬砖工岗位说明书 / 技术标准】（规定了所有打工人都必须会造对象）
 * <p>设计思想：使用了<b>策略模式 (Strategy Pattern)</b>。它将“如何创建对象”这一行为抽象出来，
 * 使得工厂可以在运行时动态选择方案。遵循了<b>里氏替换原则 (LSP)</b>，子类实现可透明替换。
 *
 * @author SnWalker
 */
public interface InstantiationStrategy {

    /**
     * 根据 Bean 定义信息实例化对象。
     *
     * @param beanDefinition 包含 Bean 的 Class 类型等信息
     * @return 实例化后的 Bean 对象
     * @throws BeansException 如果实例化过程中发生异常
     */
    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
