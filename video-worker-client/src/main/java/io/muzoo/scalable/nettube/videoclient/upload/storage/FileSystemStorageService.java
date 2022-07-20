package io.muzoo.scalable.nettube.videoclient.upload.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import io.muzoo.scalable.nettube.videoclient.upload.Video;
import io.muzoo.scalable.nettube.videoclient.upload.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path root;
    private final String cdn;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.root = Paths.get(properties.getLocation());
        this.cdn = properties.getCdn();
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    @Override
    public void save(MultipartFile file, String filename) throws IOException, NullPointerException {
        if (!file.getOriginalFilename().equals(filename)){
            File inputFile = this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())).toFile();
            File toRename = this.root.resolve(filename).toFile();
            if (toRename.exists())
                throw new IOException("Filename already exists");
            boolean success = inputFile.renameTo(toRename);
            if (!success)
                throw new StorageException("failed to rename file");
        }
        filename = file.getOriginalFilename();
        String[] arrOfFilename = filename.split("\\.");
        String[] withoutResolution = Arrays.copyOfRange(arrOfFilename,0,arrOfFilename.length-2);
        String filenameWithoutResolution = String.join(".", withoutResolution);
        if (videoRepository.existsByFilename(filenameWithoutResolution))
            throw new SocketException("video already stored!");
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        Video video = new Video();
        video.setFilename(filenameWithoutResolution);
        video.setUrl(cdn+"/"+filenameWithoutResolution+"/master.m3u8");
        videoRepository.save(video);
    }
    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
