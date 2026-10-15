package com.lsxuan.udilabel.infrastructure.storage;

public interface FileStorage {

    String upload(byte[] content, String objectKey);
}
