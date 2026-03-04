package org.springframework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

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
        //TODO 感兴趣的小伙伴可以实现下

        //throw new UnsupportedOperationException("CGLIB instantiation strategy is not supported");
        //
        // 1. 创建 CGLIB 核心类
        Enhancer enhancer = new Enhancer();
        // 2. 指定要创建谁的子类
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        // 3. 设置拦截器（这里直接调用父类方法，即原始类的逻辑）
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return proxy.invokeSuper(obj, args);
            }
        });
        // 4. 创建并返回实例
        return enhancer.create();
    }
}
