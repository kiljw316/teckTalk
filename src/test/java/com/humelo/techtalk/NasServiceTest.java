package com.humelo.techtalk;

import com.humelo.techtalk.nas.NasService;
import com.humelo.techtalk.nas.NetworkAttachedStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class NasServiceTest {

    NetworkAttachedStorage nas = new NetworkAttachedStorage();
    NasService nasService = new NasService("src/test/resources/nas", nas);

    @BeforeEach
    void setUp() throws IOException {
        nas.removeDirectory(Path.of("src/test/resources/nas"));
    }

    @AfterEach
    void tearDown() throws IOException {
        nas.removeDirectory(Path.of("src/test/resources/nas"));
    }

    @DisplayName("파일 쓰고 읽기")
    @Test
    void test1() throws IOException {
        //given
        String serviceCode = "001WEB001";
        String userEmail = "test@test.com";
        String sampleText = "sample text";

        //when
        String result = nasService.writeContentsAndRead(serviceCode, userEmail, sampleText);

        //then
        assertThat(result).isEqualTo(sampleText);
    }
}