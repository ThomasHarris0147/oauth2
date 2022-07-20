package io.muzoo.scalable.nettube.videoclient;

import io.muzoo.scalable.nettube.videoclient.upload.storage.FileSystemStorageService;
import io.muzoo.scalable.nettube.videoclient.upload.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.Resource;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BackendApplication implements CommandLineRunner {
	@Resource
	FileSystemStorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
}
