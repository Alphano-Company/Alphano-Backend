package com.alphano.alphano.domain.user.dao;

import com.alphano.alphano.domain.submission.domain.Submission;
import com.alphano.alphano.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
