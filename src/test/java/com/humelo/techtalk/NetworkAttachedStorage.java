package com.humelo.techtalk;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface NetworkAttachedStorage {
    Path createFile(Path path) throws IOException;

    Path createDirectory(Path path) throws IOException;

    Path readFile(Path path) throws IOException;

    Path writeFile(Path path, String contents) throws IOException;

    Path copyFile(Path source, Path target) throws IOException;

    boolean deleteFile(Path path) throws IOException;

    List<Path> searchFile(Path path) throws IOException;

    boolean exists(Path path) throws IOException;

    List<Path> list(Path path) throws IOException;
}
