package com.humelo.techtalk.nas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkAttachedStorage {

    public Path createFile(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.createFile(filePath);
    }

    public Path createDirectory(String path) throws IOException {
        Path directoryPath = Path.of(path);
        return Files.createDirectories(directoryPath);
    }

    public boolean removeDirectory(String path) throws IOException {
        Path directoryPath = Path.of(path);

        Files.walk(directoryPath)
                .sorted(Comparator.reverseOrder()) // 하위 디렉터리부터 삭제
                .map(Path::toFile)
                .forEach(File::delete);

        return Files.deleteIfExists(directoryPath);
    }

    public Path readFile(String path) {
        return Path.of(path);
    }

    public Path writeFile(String path, String contents) throws IOException {
        Path filePath = Path.of(path);
        return Files.writeString(filePath, contents);
    }

    public Path copyFile(String source, String target) throws IOException {
        return Files.copy(Path.of(source), Path.of(target));
    }

    public boolean deleteFile(String path) throws IOException {
        return Files.deleteIfExists(Path.of(path));
    }

    public List<Path> searchFile(String path, String searchTerm) throws IOException {
        return Files.walk(Path.of(path))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public boolean exists(String path) {
        return Files.exists(Path.of(path));
    }

    public List<Path> list(String path) throws IOException {
        return Files.list(Path.of(path))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

    }
}
