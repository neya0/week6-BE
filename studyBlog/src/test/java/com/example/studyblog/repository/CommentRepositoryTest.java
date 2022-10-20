package com.example.studyblog.repository;

import com.example.studyblog.domain.Comment;
import com.example.studyblog.domain.Gender;
import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private static Member member;
    private static Post post;

    @BeforeAll
    static void setUp() {
        member = Member.builder()
                .email("test1")
                .gender(Gender.MALE)
                .nickname("test1")
                .password("test1qwe")
                .build();

        post = Post.builder()
                .title("test1")
                .content("test1")
                .member(member)
                .build();

    }

    @Test
    void save(){
        memberRepository.save(member);
        postRepository.save(post);
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content("test")
                .build();
        Comment comment1 = commentRepository.save(comment);
        assertNotNull(comment1);
    }

    @Test
    void findAllByPost(){
        memberRepository.save(member);
        postRepository.save(post);
        for (int i=0; i<100;i++){
            Comment comment = Comment.builder()
                    .post(post)
                    .member(member)
                    .content("test")
                    .build();
            commentRepository.save(comment);
        }
        PageRequest pageRequest = PageRequest.of(0,100);
        List<Comment> commentList= commentRepository.findAllByPost(post, pageRequest);
        assertEquals(100, (long) commentList.size());

    }


    @Test
    void deleteComment(){
        memberRepository.save(member);
        Long memberId = member.getId();

        postRepository.save(post);
        Long postId = post.getId();

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content("test")
                .build();
        commentRepository.save(comment);
        Long commentId = comment.getId();

        commentRepository.delete(comment);

        assertNull(commentRepository.findById(commentId).orElse(null));
        assertNotNull(memberRepository.findById(memberId).orElse(null));
        assertNotNull(postRepository.findById(postId).orElse(null));

    }
}