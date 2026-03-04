package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.ioc.bean.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception {
        // 创建容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 构造bean的属性信息
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "jack"));
        propertyValues.addPropertyValue(new PropertyValue("age", 20));
        // 构造bean的图纸
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        // 注册bean的图纸
        beanFactory.registerBeanDefinition("person", beanDefinition);

        // 获取bean
        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("jack");
        assertThat(person.getAge()).isEqualTo(20);


    }
}
