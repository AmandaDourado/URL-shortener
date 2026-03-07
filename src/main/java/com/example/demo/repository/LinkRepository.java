package com.example.demo.repository;

import com.example.demo.entities.Link;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("SELECT l FROM Link l WHERE l.code = :code")
    Link findByCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("UPDATE Link l SET l.clicks = l.clicks + 1 WHERE l.code = :code")
    void updateClicksByCode(@Param("code") String code);
}

