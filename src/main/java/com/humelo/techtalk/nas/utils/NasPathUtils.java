package com.humelo.techtalk.nas.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class NasPathUtils {

    private final String rootPath;

    public NasPathUtils(@Value("${root-path}") String rootPath) {
        this.rootPath = rootPath;
    }


    public static class NasPath {
        private final Path path;

        public NasPath(Path path) {
            this.path = path;
        }

        public Path getPath() {
            return path;
        }
    }
}
