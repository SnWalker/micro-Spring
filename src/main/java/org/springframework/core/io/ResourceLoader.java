package org.springframework.core.io;

/**
 * 资源加载器
 */
public interface ResourceLoader {

    Resource getResource(String location);
}
