package com.fiap.techchallenge14.user.repository;

import com.fiap.techchallenge14.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByLoginAndPassword(String login, String password);
}

