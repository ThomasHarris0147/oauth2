package io.muzoo.scalable.nettube.videoclient.repository;

import io.muzoo.scalable.nettube.videoclient.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends
        JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);
}