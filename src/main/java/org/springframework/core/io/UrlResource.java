package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 用于处理 URL 定位的资源
 *
 */
public class UrlResource implements Resource {

    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    /**
     * 获取 URL 资源的输入流
     * 首先建立与 URL 的连接，然后返回该连接的输入流
     *
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        // 1. 打开 URL 连接
        URLConnection con = this.url.openConnection();
        try {
            // 2. 尝试获取并返回底层输入流
            return con.getInputStream();
        } catch (IOException ex) {
            // 3. 如果在获取流时发生异常，直接向上抛出
            throw ex;
        }
    }
}
