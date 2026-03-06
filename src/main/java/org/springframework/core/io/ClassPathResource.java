package org.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用于获取类路径（Classpath）下的资源文件
 * 例如：src/main/resources 目录下的配置文件
 */
public class ClassPathResource implements Resource {

    private final String path;

    public ClassPathResource(String path) {
        this.path = path;
    }

    /**
     * 获取类路径资源的输入流
     * @return
     * @throws IOException 如果获取流的过程中发生错误
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.path);
        if (is == null) {
            throw new FileNotFoundException(this.path + "does not exit");
        }
        return is;
    }
}
