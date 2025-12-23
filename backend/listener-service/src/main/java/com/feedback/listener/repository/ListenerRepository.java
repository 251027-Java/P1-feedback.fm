package com.feedback.listener.repository;

import com.feedback.listener.model.Listener;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListenerRepository extends JpaRepository<Listener, String> {
    Optional<Listener> findByEmail(String email);
}
