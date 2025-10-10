package com.fiap.techchallenge14.user.repository;

import com.fiap.techchallenge14.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByUsernameAndPassword(String username, String password);
}

