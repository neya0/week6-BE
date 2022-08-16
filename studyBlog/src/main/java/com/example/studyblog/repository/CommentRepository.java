package com.example.studyblog.repository;

import com.example.studyblog.domain.Comment;
import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post, PageRequest pageRequest);
    List<Comment> findAllByMember(Member member);
}
