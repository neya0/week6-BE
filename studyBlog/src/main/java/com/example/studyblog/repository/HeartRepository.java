package com.example.studyblog.repository;

import com.example.studyblog.domain.Comment;
import com.example.studyblog.domain.Heart;
import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Heart.HeartId> {
    Long countHeartById_Post(Post post);
    Long countHeartById_Comment(Comment comment);
    Long countHeartById_Member(Member member);
}
