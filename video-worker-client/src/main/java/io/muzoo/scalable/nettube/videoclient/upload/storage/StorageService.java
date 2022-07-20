package io.muzoo.scalable.nettube.videoclient.upload.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();
    void save(MultipartFile file, String filename) throws IOException;
    Resource load(String filename);
    void deleteAll();
    Stream<Path> loadAll();
}