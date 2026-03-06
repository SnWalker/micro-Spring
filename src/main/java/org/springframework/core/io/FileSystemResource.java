package org.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * 获取文件系统中的资源
 * 例如，D:/a.txt
 */
public class FileSystemResource implements Resource {

    private final String filePath;

    public FileSystemResource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            // 1. 将字符串路径转换为 Path 对象
            Path path = new File(this.filePath).toPath();
            // 2. 尝试打开并返回文件输入流
            // 如果文件不存在，底层会直接抛出 NoSuchFileException
            return Files.newInputStream(path);
        } catch (NoSuchFileException ex) {
            // 3. 显式捕获并重新抛出 NoSuchFileException
            throw new NoSuchFileException(ex.getMessage());
        }
    }

}
