package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.ioc.bean.Car;
import org.springframework.test.ioc.bean.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class PopulateBeanWithPropertyValuesTest {
    /**
     * 为bean注入属性
     *
     * @throws Exception
     */
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

    /**
     * 为bean注入bean
     *
     * @throws Exception
     */
    @Test
    public void testPopulateBeanWithBean() throws Exception {
        // 创建容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //注册Car实例
        PropertyValues propertyValuesForCar = new PropertyValues();
        propertyValuesForCar.addPropertyValue(new PropertyValue("brand", "BWM"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, propertyValuesForCar);
        beanFactory.registerBeanDefinition("car", carBeanDefinition);

        //注册Person实例
        PropertyValues propertyValuesForPerson = new PropertyValues();
        propertyValuesForPerson.addPropertyValue(new PropertyValue("name", "jack"));
        propertyValuesForPerson.addPropertyValue(new PropertyValue("age", 18));
        //Person实例依赖Car实例
        propertyValuesForPerson.addPropertyValue(new PropertyValue("car", new BeanReference("car")));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValuesForPerson);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("jack");
        assertThat(person.getAge()).isEqualTo(18);
        Car car = person.getCar();
        assertThat(car).isNotNull();
        assertThat(car.getBrand()).isEqualTo("BWM");
    }
}
