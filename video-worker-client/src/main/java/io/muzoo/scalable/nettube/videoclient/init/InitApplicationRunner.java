package io.muzoo.scalable.nettube.videoclient.init;

import io.muzoo.scalable.nettube.videoclient.repository.UserRepository;
import io.muzoo.scalable.nettube.videoclient.upload.Video;
import io.muzoo.scalable.nettube.videoclient.upload.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //Add default admin user and set its password if missing (EP4)
        /*User admin = userRepository.findFirstByUsername("admin");
        if (admin == null){
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole("USER");
            userRepository.save(admin);
        }*/
        boolean spy1 = videoRepository.existsByFilename("spy-x-family-EP.1");
        if (!spy1) {
            Video video = new Video();
            video.setUrl("d2tsi3g28pn9oo.cloudfront.net/spy-x-family-EP.1/master.m3u8");
            video.setFilename("spy-x-family-EP.1");
            videoRepository.save(video);
        }
        boolean spy2 = videoRepository.existsByFilename("spy-x-family-EP.2");
        if (!spy2) {
            Video video = new Video();
            video.setUrl("d2tsi3g28pn9oo.cloudfront.net/spy-x-family-EP.2/master.m3u8");
            video.setFilename("spy-x-family-EP.2");
            videoRepository.save(video);
        }
        boolean spy3 = videoRepository.existsByFilename("spy-x-family-EP.3");
        if (!spy3) {
            Video video = new Video();
            video.setUrl("d2tsi3g28pn9oo.cloudfront.net/spy-x-family-EP.3/master.m3u8");
            video.setFilename("spy-x-family-EP.3");
            videoRepository.save(video);
        }
    }
}
