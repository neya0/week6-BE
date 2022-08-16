package com.example.studyblog.service;

import com.example.studyblog.controller.request.CommentRequestDto;
import com.example.studyblog.controller.response.CommentResponseDto;
import com.example.studyblog.controller.response.MsgResponseDto;
import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.domain.Comment;
import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.Post;
import com.example.studyblog.repository.CommentRepository;
import com.example.studyblog.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;

    private static Member member;
    private static Post post;

    @BeforeAll
    static void setUp() {
        member = Member.builder()
                .memberId("test1")
                .gender(Member.Gender.MALE)
                .nickName("test1")
                .password("test1qwe")
                .id(1L)
                .build();

        post = Post.builder()
                .title("test1")
                .content("test1")
                .id(1L)
                .member(member)
                .build();
    }

    @Test
    @DisplayName("Comment 작성 성공")
    void createComment1() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("test1")
                .build();
        ResponseDto<CommentResponseDto> responseDto = commentService.createComment(1L, commentRequestDto, member);
        assertTrue(responseDto.isSuccess());
    }

    @Test
    @DisplayName("Comment 작성 실패-게시글 없음")
    void createComment2() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("test1")
                .build();
        ResponseDto<CommentResponseDto> responseDto = commentService.createComment(1L, commentRequestDto, member);
        assertFalse(responseDto.isSuccess());
        assertEquals(responseDto.getError().getCode(), "POST_NOT_FOUND");
    }

    @Test
    @DisplayName("Comment 삭제 성공")
    void deleteComment1(){
        Comment comment = Comment.builder()
                .content("test")
                .member(member)
                .post(post)
                .id(1L)
                .build();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();

        ResponseDto<MsgResponseDto> responseDto = commentService.deleteComment(1L);
        assertEquals(responseDto.getData().getMsg(),"댓글을 삭제했습니다.");
    }

    @Test
    @DisplayName("Comment 삭제 실패-댓글 없음")
    void deleteComment2(){
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        doNothing().when(commentRepository).delete(any(Comment.class));
        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();

        ResponseDto<?> responseDto = commentService.deleteComment(1L);
        assertEquals(responseDto.getError().getCode(),"COMMENT_NOT_FOUND");
    }

    @Test
    @DisplayName("Comment 수정 성공")
    void updateComment1(){
        Comment comment = Comment.builder()
                .content("test")
                .member(member)
                .post(post)
                .id(1L)
                .build();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();
        ResponseDto<CommentResponseDto> responseDto = commentService.updateComment(1L, new CommentRequestDto("test123"));
        assertTrue(responseDto.isSuccess());
    }

    @Test
    @DisplayName("Comment 수정 실패-댓글 없음")
    void updateComment2(){
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();

        ResponseDto<CommentResponseDto> responseDto = commentService.updateComment(1L, new CommentRequestDto("test123"));
        assertEquals(responseDto.getError().getCode(),"COMMENT_NOT_FOUND");
    }

    @Test
    @DisplayName("Comment 가져오기 성공")
    void getComment1(){
        List<Comment> commentList = new ArrayList<>();
        for (long i=0; i<1000; i++){
            Comment comment = Comment.builder()
                    .content(String.format("test%d", i))
                    .member(member)
                    .post(post)
                    .id(i+1)
                    .build();
            commentList.add(comment);
        }

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        for (long i=0;i<(1000/50);i++) {
            when(commentRepository.findAllByPost(post, PageRequest.of((int) i, 50))).thenReturn(commentList.subList((int) i*50,(int) (i+1)*50));
        }
        CommentService commentService = CommentService.builder()
                .postRepository(postRepository)
                .commentRepository(commentRepository)
                .build();

        for (long i=0;i<(1000/50);i++){
            ResponseDto<List<CommentResponseDto>> responseDto = commentService.getComment(1L, 50, (int) i);
            assertTrue(responseDto.isSuccess());
            assertEquals(responseDto.getData().get(0).getId(),i*50+1);
        }

    }
}