package com.humelo.techtalk.nas;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface NetworkAttachedStorage {
    Path createFile(String path) throws IOException;

    Path createDirectory(String path) throws IOException;

    boolean removeDirectory(String path) throws IOException;

    Path readFile(String path);

    Path writeFile(String path, String contents) throws IOException;

    Path copyFile(String source, String target) throws IOException;

    boolean deleteFile(String path) throws IOException;

    List<Path> searchFile(String path, String searchTerm) throws IOException;

    boolean exists(String path);

    List<Path> list(String path) throws IOException;
}
