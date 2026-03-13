package org.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Method;

/**
 * 具备自动装配能力的抽象工厂。
 *
 * <p>角色定位：【全能型生产小组长】（指挥工人搬砖，预留装修接口）
 * <p>设计思想：使用了<b>策略模式 (Strategy Pattern)</b>。它不直接负责 new 对象，
 * 而是通过 InstantiationStrategy 接口（搬砖策略）在运行时动态选择最合适的方案。
 * 遵循了<b>单一职责原则 (SRP)</b>：组长负责控制“生产流程”，具体的“搬砖动作”交给策略类。
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    /** 实例化策略：负责具体的对象创建动作（真正的苦力活在这里） */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(beanName, beanDefinition);
    }

    /**
     * 执行具体的创建流程。
     */
    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            // 委派给专门的策略模式方法进行实例化（把造对象的脏活累活包给专业的打工人）
            bean = createBeanInstance(beanDefinition);
            // 为 bean 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate [" + beanName + "]", e);
        }

        //注册有销毁方法的bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);


        // 将创建好的成品/半成品加入单例缓存池（将成品运入成品仓库存档）
        addSingleton(beanName, bean);
        return bean;
    }

    /**
     * 注册有销毁方法的bean，即bean继承自DisposableBean或有自定义的销毁方法
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }

    /**
     * 实例化委派方法。
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition) throws BeansException {
        // 调用具体的实例化策略（如反射或 CGLIB）
        return this.getInstantiationStrategy().instantiate(beanDefinition);
    }

    /**
     * 为bean填充属性
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        //TODO 暂时不支持解决循环依赖
        try {
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
                // 1、获取bean的一条属性信息（包括名称、值）
                String name = propertyValue.getName(); // 获得属性名 例如，age
                Object value = propertyValue.getValue(); // 获得属性值 例如，18
                // 2、若不是普通属性，而是bean（当前value并不是beanA依赖的beanB,而是统一封装成的beanReference，其中存储了beanB的信息）
                if (value instanceof BeanReference) {
                    // beanA依赖beanB，先实例化beanB
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                // 3、通过反射设置属性
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception ex) {
            throw new BeansException("Failed to populate property values for " + beanName, ex);
        }

    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 执行BeanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Throwable ex) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", ex);
        }

        // 执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 执行bean的初始化方法
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @throws Throwable
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Throwable{
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName) && !(bean instanceof InitializingBean && "afterPropertiesSet".equals(initMethodName))) {
            Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getBeanClass(), initMethodName);
            if (initMethod == null) {
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(bean);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
