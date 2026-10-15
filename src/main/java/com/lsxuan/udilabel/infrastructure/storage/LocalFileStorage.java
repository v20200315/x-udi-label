package com.lsxuan.udilabel.infrastructure.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorage implements FileStorage {

    private final Path storageRoot;

    public LocalFileStorage() {
        this.storageRoot = Paths.get("uploads").toAbsolutePath().normalize();
    }

    @Override
    public String upload(byte[] content, String objectKey) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("File content cannot be empty.");
        }

        if (objectKey == null || objectKey.isBlank()) {
            throw new IllegalArgumentException("Object key cannot be empty.");
        }

        Path target = storageRoot.resolve(objectKey).normalize();

        // 防止 objectKey 使用 ../ 写入存储目录之外
        if (!target.startsWith(storageRoot)) {
            throw new IllegalArgumentException("Invalid object key.");
        }

        try {
            Files.createDirectories(target.getParent());
            Files.write(target, content);

            return "/uploads/" + objectKey.replace("\\", "/");
        } catch (IOException e) {
            throw new FileStorageException("Failed to save file locally: " + objectKey, e);
        }
    }
}