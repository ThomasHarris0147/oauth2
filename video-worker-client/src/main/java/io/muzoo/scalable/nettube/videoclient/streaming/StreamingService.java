package io.muzoo.scalable.nettube.videoclient.streaming;

import io.muzoo.scalable.nettube.videoclient.upload.Video;

import java.util.List;

public interface StreamingService {
    List<Video> getAllVideos();
    Video getVideoById(Long id);
}
