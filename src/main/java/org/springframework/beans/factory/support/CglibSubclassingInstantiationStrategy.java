package org.springframework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 基于 CGLIB 的实例化策略。
 *
 * <p>角色定位：【高级特种技工】
 * <p>职责说明：通过动态生成目标类的子类（CGLIB 字节码增强）来创建实例。
 * 这种方式不依赖于简单的反射，能够更好地处理复杂的代理或增强需求。
 *
 * @author SnWalker
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    /**
     * 使用 CGLIB 动态生成子类并实例化。
     *
     * @param beanDefinition Bean 定义
     * @return CGLIB 增强后的实例化对象
     * @throws BeansException 实例化失败异常
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanDefinition.getBeanClass());
            // 设置拦截器，直接调用父类（原对象）的方法
            enhancer.setCallback((MethodInterceptor) (obj, method, argsTemp, proxy) -> proxy.invokeSuper(obj, argsTemp));
            return enhancer.create();
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate [" + beanDefinition.getBeanClass().getName() + "] via CGLIB", e);
        }
    }
}
