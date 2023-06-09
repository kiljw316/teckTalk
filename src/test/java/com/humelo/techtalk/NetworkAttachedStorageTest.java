package com.humelo.techtalk;

import com.humelo.techtalk.nas.NetworkAttachedStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class NetworkAttachedStorageTest {

    /*
     * Path 인터페이스 또는 File 객체를 통해 파일 시스템에 접근
     * 클라이언트(프론트엔드)는 직접 접근할 수 없고 어플리케이션을 프록시로 사용함
     * NAS 에 직접 접근하는 것처럼 동작하는 클래스
     * /nas 디렉터리를 생성해야 한다.
     * NAS 는 경로를 모른다. 요청 온 경로에 해당 작업만 실행시킬뿐
     */
    private static final Path ROOT_PATH = Path.of("src/test/resources/nas");

    private static final Path TEST_FILE1_PATH = ROOT_PATH.resolve("testFile.txt");
    private static final String TEST_FILE1_CONTENTS = "hello world";

    private static final Path TEST_FILE2_PATH = ROOT_PATH.resolve("testFile2.txt");
    private static final String TEST_FILE2_CONTENTS = "hello world2";

    private static final Path TEST_FILE_COPY_PATH = ROOT_PATH.resolve("testCopyFile.txt");
    private static final Path TEST_DELETE_FILE_PATH = ROOT_PATH.resolve("testDeleteFile.txt");

    private static final String SEARCH_TERM = "test";

    NetworkAttachedStorage nas = new NetworkAttachedStorage();

    @BeforeEach
    void setUp() throws IOException {
        nas.createDirectory(ROOT_PATH);

        nas.createFile(TEST_FILE1_PATH);
        nas.createFile(TEST_FILE2_PATH);

        nas.writeFile(TEST_FILE1_PATH, TEST_FILE1_CONTENTS);
        nas.writeFile(TEST_FILE2_PATH, TEST_FILE2_CONTENTS);
    }

    @AfterEach
    public void tearDown() throws IOException {
        nas.removeDirectory(ROOT_PATH);
    }

    @DisplayName("파일 생성")
    @Test
    void createTempFile() throws IOException {
        //given
        String contents = "hello world";
        Path filePath = ROOT_PATH.resolve("test.txt");
        Path file = nas.createFile(filePath);
        nas.writeFile(filePath, contents);

        //when
        String actual = new String(Files.readAllBytes(file));

        //then
        assertThat(actual).isEqualTo(contents);
    }

    @DisplayName("파일 읽기")
    @Test
    void readFile() throws IOException {
        //when
        String fileContents = nas.readString(TEST_FILE1_PATH);

        //then
        assertThat(fileContents).isEqualTo(TEST_FILE1_CONTENTS);
    }

    @DisplayName("파일 쓰기")
    @Test
    void writeFile() throws IOException {
        //given
        String newContents = "new file contents";

        //when
        Path filePath = nas.writeFile(TEST_FILE1_PATH, newContents);

        //then
        String fileContents = Files.readString(filePath);
        assertThat(fileContents).isEqualTo(newContents);
    }

    @DisplayName("파일 복사")
    @Test
    void copyFile() throws IOException {
        //when
        Path targetFile = nas.copyFile(TEST_FILE1_PATH, TEST_FILE_COPY_PATH);

        //then
        String targetFileContents = Files.readString(targetFile);
        assertThat(TEST_FILE1_CONTENTS).isEqualTo(targetFileContents);
    }

    @DisplayName("파일 삭제")
    @Test
    void deleteFile() throws IOException {
        //given
        Path targetFile = nas.copyFile(TEST_FILE1_PATH, TEST_DELETE_FILE_PATH);

        //when
        boolean isDeleted = nas.deleteFile(TEST_DELETE_FILE_PATH);

        //then
        assertThat(isDeleted).isTrue();
        assertThat(Files.exists(targetFile)).isFalse();
    }

    @DisplayName("파일 검색")
    @Test
    void searchFile() throws IOException {
        //when
        List<Path> matchingFiles = nas.searchFile(ROOT_PATH, SEARCH_TERM);

        //then
        assertThat(matchingFiles).hasSize(2);
    }

    @DisplayName("파일 존재 유무 확인")
    @Test
    void isExistFile() {
        //when
        boolean isExists = nas.exists(TEST_FILE1_PATH);

        //then
        assertThat(isExists).isTrue();
    }

    @DisplayName("파일 목록 조회")
    @Test
    void getFileList() throws IOException {
        //when
        List<Path> filePathList = nas.list(ROOT_PATH);

        //then
        assertThat(filePathList).hasSize(2);
    }

    @DisplayName("디렉터리 생성")
    @Test
    void mkdirs() throws IOException {
        //given
        Path tempDirectoryPath = ROOT_PATH.resolve("testDir");

        //when
        Path directory = nas.createDirectory(tempDirectoryPath);
        boolean isDirectory = Files.isDirectory(directory);

        //then
        assertThat(isDirectory).isTrue();
    }

    @DisplayName("파일 읽기(바이트)")
    @Test
    void readFileBytes() throws IOException {
        //when
        byte[] contentsByteArray = nas.readAllBytes(TEST_FILE1_PATH);

        //then
        assertThat(new String(contentsByteArray, StandardCharsets.UTF_8)).isEqualTo(TEST_FILE1_CONTENTS);
    }

    @DisplayName("파일 인풋 스트림")
    @Test
    void inputStream() throws IOException {
        //given
        InputStream inputStream = nas.newInputStream(TEST_FILE1_PATH);

        //when
        byte[] contentsByteArray = inputStream.readAllBytes();

        //then
        assertThat(new String(contentsByteArray, StandardCharsets.UTF_8)).isEqualTo(TEST_FILE1_CONTENTS);
    }

    @DisplayName("파일 존재 유무 확인(없을 경우)")
    @Test
    void isNotExistFile() {
        //given
        Path filePath = ROOT_PATH.resolve("notExistsFile.txt");

        //when
        boolean notExists = nas.notExists(filePath);

        //then
        assertThat(notExists).isTrue();
    }

    @DisplayName("파일 아웃풋 스트림")
    @Test
    void outputStream() throws IOException {
        //given
        Path filePath = nas.createFile(ROOT_PATH.resolve("test.txt"));
        OutputStream outputStream = nas.newOutputStream(filePath);

        String contents = "hello world";
        outputStream.write(contents.getBytes(StandardCharsets.UTF_8));

        //when
        String fileContents = nas.readString(filePath);

        //then
        assertThat(fileContents).isEqualTo(contents);
    }
}
