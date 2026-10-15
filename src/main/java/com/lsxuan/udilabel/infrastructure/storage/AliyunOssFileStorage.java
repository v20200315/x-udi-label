package com.lsxuan.udilabel.infrastructure.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "oss")
public class AliyunOssFileStorage implements FileStorage {

    @Override
    public String upload(byte[] content, String objectKey) {
        throw new UnsupportedOperationException("Implement OSS upload");
    }
}
