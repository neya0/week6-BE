package com.example.studyblog.repository;

import com.example.studyblog.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Heart.HeartId> {
    Long countHeartById(Heart.HeartId heartId);
}
