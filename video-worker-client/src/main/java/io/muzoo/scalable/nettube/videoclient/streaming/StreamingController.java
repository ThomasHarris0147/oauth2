package io.muzoo.scalable.nettube.videoclient.streaming;

import io.muzoo.scalable.nettube.videoclient.upload.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StreamingController {

    @Autowired
    AwsS3StreamingService streamingService;

    @GetMapping("/listVideos")
    public ResponseEntity<List<Video>> getVideos() {
        List<Video> objects = streamingService.getAllVideos();
        return ResponseEntity.ok().body(objects);
    }

    @GetMapping("/getVideo/{id:.+}")
    @ResponseBody
    public ResponseEntity<Video> serveVideo(
            @PathVariable Long id) {
        Video video = streamingService.getVideoById(id);
        if (video == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(video);
    }
}
