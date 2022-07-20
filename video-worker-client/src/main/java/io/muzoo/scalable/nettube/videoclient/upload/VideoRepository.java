package io.muzoo.scalable.nettube.videoclient.upload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video,String> {
    Video getVideoById(Long id);
    List<Video> findAll();
    Boolean existsByFilename(String filename);
}
