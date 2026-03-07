package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.test.ioc.bean.Car;
import org.springframework.test.ioc.bean.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlFileDefineBeanTest {

    @Test
    public void testXmlFile() throws Exception {
        // 创建容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 加载.xml配置文件，构造并遍历DOM树，实例化BeanDefinition并注册到容器
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        // 获取bean -> bean不存在 -> 获取 beanDefinition -> 实例化bean并缓存到单例池 ,返回bean
        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("snwalker");
        assertThat(person.getCar().getBrand()).isEqualTo("porsche");

        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);
        assertThat(car.getBrand()).isEqualTo("porsche");
    }
}
