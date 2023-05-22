package com.humelo.techtalk.nas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkAttachedStorage {

    public Path createFile(Path path) throws IOException {
        return Files.createFile(path);
    }

    public Path createDirectory(Path path) throws IOException {
        return Files.createDirectories(path);
    }

    public boolean removeDirectory(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder()) // 하위 디렉터리부터 삭제
                .map(Path::toFile)
                .forEach(File::delete);

        return Files.deleteIfExists(path);
    }

    public String readString(Path path) throws IOException {
        return Files.readString(path);
    }

    public Path writeFile(Path path, String contents) throws IOException {
        return Files.writeString(path, contents);
    }

    public Path copyFile(Path source, Path target) throws IOException {
        return Files.copy(source, target);
    }

    public boolean deleteFile(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public List<Path> searchFile(Path path, String searchTerm) throws IOException {
        return Files.walk(path)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public List<Path> list(Path path) throws IOException {
        return Files.list(path)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

    }
}
