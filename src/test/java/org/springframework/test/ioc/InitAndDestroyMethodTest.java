package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InitAndDestroyMethodTest {

    @Test
    public void testInitAndDestroyMethod() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");

        // 向JVM注册一个钩子方法，在JVM关机前自动触发，清理容器
        applicationContext.registerShutdownHook();  // 或者手动关闭 applicationContext.close();
    }
}
