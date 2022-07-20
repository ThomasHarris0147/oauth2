package io.muzoo.scalable.nettube.videoclient.repository;

import io.muzoo.scalable.nettube.videoclient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}