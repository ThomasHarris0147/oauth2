package io.muzoo.scalable.nettube.videoclient.streaming;

import io.muzoo.scalable.nettube.videoclient.upload.Video;
import io.muzoo.scalable.nettube.videoclient.upload.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AwsS3StreamingService implements StreamingService {
    @Autowired
    VideoRepository videoRepository;
    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(Long id) {
        return videoRepository.getVideoById(id);
    }
}
