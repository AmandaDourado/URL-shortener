package com.example.demo.repository;

import com.example.demo.entities.Link;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("SELECT l FROM Link l WHERE l.code = :code")
    Optional<Link> findByCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("UPDATE Link l SET l.clicks = l.clicks + 1 WHERE l.code = :code")
    void updateClicksByCode(@Param("code") String code);

    @Query("SELECT l FROM Link l WHERE l.secretKey = :secretKey")
    Optional<Link> findBySecretKey(@Param("secretKey") String secretKey);

    @Modifying
    @Transactional
    @Query("DELETE FROM Link l WHERE l.expires < :localDateTime")
    void deleteExpiredLinks(@Param("localDateTime") LocalDateTime localDateTime);
}
