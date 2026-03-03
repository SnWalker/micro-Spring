package org.springframework.beans.factory;

/**
 * Bean 体系下的运行时异常基类。
 *
 * <p>角色定位：【统一报警器】
 * <p>职责说明：封装了 Bean 生命周期中可能出现的所有错误。
 * 它继承自 RuntimeException，意味着不必在业务代码中强制捕获，保持代码整洁。
 */
public class BeansException extends RuntimeException {

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
