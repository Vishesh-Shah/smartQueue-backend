package com.smartqueue.repository;

import com.smartqueue.model.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {
    List<AdminRequest> findAllByOrderByCreatedAtDesc();
    Optional<AdminRequest> findByEmailAndStatus(String email, String status);
    boolean existsByEmail(String email);
}