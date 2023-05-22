package com.humelo.techtalk.nas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class NasService {

    private final NetworkAttachedStorage nas;

    private final Path ROOT_PATH;
    private final Path VOICE_STATIC_PATH;
    private final Path MAIL_TEMPLATE_PATH;
    private final Path RESERVATION_MAIL_PATH;
    private final Path API_PATH;


    public NasService(@Value("${root-path}") String rootPath, NetworkAttachedStorage nas) {
        this.nas = nas;
        this.ROOT_PATH = Path.of(rootPath);
        this.VOICE_STATIC_PATH = ROOT_PATH.resolve("static");
        this.MAIL_TEMPLATE_PATH = ROOT_PATH.resolve("static/templates");
        this.RESERVATION_MAIL_PATH = ROOT_PATH.resolve("mail/reservation");
        this.API_PATH = ROOT_PATH.resolve("api");

        try {
            if (nas.notExists(ROOT_PATH)) {
                nas.createDirectory(ROOT_PATH);
            }

            if (nas.notExists(VOICE_STATIC_PATH)) {
                nas.createDirectory(VOICE_STATIC_PATH);
            }

            if (nas.notExists(MAIL_TEMPLATE_PATH)) {
                nas.createDirectory(MAIL_TEMPLATE_PATH);
            }

            if (nas.notExists(RESERVATION_MAIL_PATH)) {
                nas.createDirectory(RESERVATION_MAIL_PATH);
            }

            if (nas.notExists(API_PATH)) {
                nas.createDirectory(API_PATH);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public String writeContentsAndRead(String serviceCode, String userEmail, String contents) throws IOException {
        final Path resourcePath = API_PATH
                .resolve(serviceCode)
                .resolve(userEmail);

        if (nas.notExists(resourcePath)) {
            nas.createDirectory(resourcePath);
        }

        Path resourceFilePath = resourcePath.resolve("test.txt");
        nas.writeFile(resourceFilePath, contents);

        return nas.readString(resourceFilePath);
    }

    public ByteArrayResource getSingleGenerateFile(String serviceCode, String userEmail) throws IOException {
        Path resourcePath = API_PATH
                .resolve(serviceCode)
                .resolve(userEmail);

        if (nas.notExists(resourcePath)) {
            throw new FileNotFoundException(); // or mkdir
        }

        byte[] byteArray = nas.readAllBytes(resourcePath);
        return new ByteArrayResource(byteArray);
    }

    public void getAudioContentsFile(String serviceCode, String userEmail) {
        Path resourcePath = API_PATH
                .resolve(serviceCode)
                .resolve(userEmail)
                .resolve("audioContents")
                .resolve("audioContents-uid")
                .resolve("audioContentsHistoryUid");

        try (BufferedInputStream inputStream = new BufferedInputStream(nas.newInputStream(resourcePath))) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // 읽은 데이터를 화면에 출력
                System.out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
