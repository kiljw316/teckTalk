package com.humelo.techtalk;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class FileTest {

    // 파일 객체는 항상 Path 로부터 시작된다.
    private static final String TEST_FILE_DIR_PATH = "src/test/resources/file";
    private static final String TEST_FILE_PATH = "src/test/resources/file/testFile.txt";
    private static final String TEST_FILE_COPY_PATH = "src/test/resources/file/testCopyFile.txt";
    private static final String TEST_DELETE_FILE_PATH = "src/test/resources/file/testDeleteFile.txt";
    private static final String SEARCH_TERM = "test";
    private static final String TEST_FILE_CONTENTS = "hello world";

    // 파일 테스트는 리소스 생성과 제거가 중요하다.
    @BeforeEach
    void setUp() throws IOException {
        Path path = Path.of(TEST_FILE_PATH);
        Files.writeString(path, TEST_FILE_CONTENTS);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE_COPY_PATH));
        Files.deleteIfExists(Path.of(TEST_DELETE_FILE_PATH));
    }

    @DisplayName("파일 생성")
    @Test
    void createTempFile() throws IOException {
        //given
        Path path = Files.createTempFile("test", ".txt");
        Files.writeString(path, "hello world");

        //when
        String actual = new String(Files.readAllBytes(path));

        //then
        assertThat(actual).isEqualTo("hello world");
    }

    @DisplayName("파일 읽기")
    @Test
    void readFile() throws IOException {
        //given
        Path path = Path.of(TEST_FILE_PATH);

        //when
        String fileContents = Files.readString(path);

        //then
        assertThat(fileContents).isEqualTo(TEST_FILE_CONTENTS);
    }

    @DisplayName("파일 쓰기")
    @Test
    void writeFile() throws IOException {
        //given
        Path path = Path.of(TEST_FILE_PATH);

        //when
        Files.writeString(path, "new file contents");

        //then
        String fileContents = Files.readString(path);
        assertThat(fileContents).isEqualTo("new file contents");
    }

    @DisplayName("파일 복사")
    @Test
    void copyFile() throws IOException {
        //given
        Path sourcePath = Path.of(TEST_FILE_PATH);
        Path targetPath = Path.of(TEST_FILE_COPY_PATH);

        //when
        Files.copy(sourcePath, targetPath);

        //then
        String sourceFileContents = Files.readString(sourcePath);
        String targetFileContents = Files.readString(targetPath);
        assertThat(sourceFileContents).isEqualTo(targetFileContents);
    }

    @DisplayName("파일 삭제")
    @Test
    void deleteFile() throws IOException {
        //given
        Path sourcePath = Path.of(TEST_FILE_PATH);
        Path targetPath = Path.of(TEST_DELETE_FILE_PATH);
        Files.copy(sourcePath, targetPath);

        //when
        Path path = Path.of(TEST_DELETE_FILE_PATH);
        Files.deleteIfExists(path);

        //then
        assertThat(Files.exists(path)).isFalse();
    }

    @DisplayName("파일 검색")
    @Test
    void searchFile() throws IOException {
        //given
        Path dirPath = Path.of(TEST_FILE_DIR_PATH);

        //when
        List<Path> matchingFiles = Files.walk(dirPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().contains(SEARCH_TERM))
                .collect(Collectors.toList());

        //then
        assertThat(matchingFiles).hasSize(3);
    }

    @DisplayName("파일 존재 유무 확인")
    @Test
    void isExistFile() {
        //given
        Path path = Path.of(TEST_FILE_PATH);

        //when
        boolean isExists = Files.exists(path);

        //then
        assertThat(isExists).isTrue();
    }

    @DisplayName("파일 목록 조회")
    @Test
    void getFileList() throws IOException {
        //given
        Path path = Path.of(TEST_FILE_DIR_PATH);

        //when
        List<String> fileNameList = Files.list(path)
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());

        //then
        assertThat(fileNameList).hasSize(3);
    }

    @DisplayName("디렉터리 생성")
    @Test
    void mkdirs() throws IOException {
        //given
        Path tempDirectoryPath = Path.of(TEST_FILE_DIR_PATH).resolve("testDir");
        Path directory = Files.createDirectory(tempDirectoryPath);

        //when
        boolean isDirectory = Files.isDirectory(directory);

        //then
        assertThat(isDirectory).isTrue();

        Files.deleteIfExists(directory);
    }
    @DisplayName("파일 모킹")
    @Test
    void mockFile() {
        // given
        File file = Mockito.mock(File.class);
        String target = "test.txt";
        Mockito.when(file.getName()).thenReturn(target);

        //when
        String actual = file.getName();

        //then
        assertThat(actual).isEqualTo(target);
    }

    @DisplayName("파일 리소스 가져오기")
    @Test
    void getResource() throws IOException {
        //given
        Path path = Path.of(TEST_FILE_PATH);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        //when
        long actual = resource.contentLength();

        //then
        assertThat(actual).isEqualTo(TEST_FILE_CONTENTS.getBytes(StandardCharsets.UTF_8).length);
    }
}
