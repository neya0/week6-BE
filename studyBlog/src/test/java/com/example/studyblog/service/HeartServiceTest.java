package com.example.studyblog.service;

import com.example.studyblog.controller.response.HeartResponseDto;
import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.domain.*;
import com.example.studyblog.jwt.TokenProvider;
import com.example.studyblog.repository.CommentRepository;
import com.example.studyblog.repository.HeartRepository;
import com.example.studyblog.repository.PostRepository;
import com.example.studyblog.shared.TokenValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeartServiceTest {

    @Mock
    private HeartRepository heartRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private TokenProvider tokenProvider;

    private static Member member;
    private static Post post;
    private static Comment comment;

    @BeforeAll
    static void setUp() {
        member = Member.builder()
                .email("test1")
                .gender(Gender.MALE)
                .nickname("test1")
                .password("test1qwe")
                .id(1L)
                .build();

        post = Post.builder()
                .title("test1")
                .content("test1")
                .id(1L)
                .member(member)
                .build();

        comment = Comment.builder()
                .post(post)
                .member(member)
                .content("test")
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    void heart1() throws Exception{
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        HeartService heartService = new HeartService(heartRepository, postRepository, commentRepository,tokenProvider,tokenValidator);
        ResponseDto<HeartResponseDto> responseDto = heartService.HeartPost(1L, member);
        assertTrue(responseDto.isSuccess());
        assertTrue(responseDto.getData().isHeart());
    }

    @Test
    @DisplayName("게시글 좋아요 실패")
    void heart2() throws Exception{
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        HeartService heartService = new HeartService(heartRepository, postRepository, commentRepository,tokenProvider,tokenValidator);
        ResponseDto<HeartResponseDto> responseDto = heartService.HeartPost(1L, member);
        assertFalse(responseDto.isSuccess());
        assertEquals("POST_NOT_FOUND",responseDto.getError().getCode());
    }

    @Test
    @DisplayName("게시글 좋아요 취소 성공")
    void heart3() throws Exception{
        Heart heart = new Heart(post,comment,member);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(heartRepository.findById(any())).thenReturn(Optional.of(heart));
        HeartService heartService = new HeartService(heartRepository, postRepository, commentRepository,tokenProvider,tokenValidator);
        ResponseDto<HeartResponseDto> responseDto = heartService.HeartPost(1L, member);
        assertTrue(responseDto.isSuccess());
        assertFalse(responseDto.getData().isHeart());
    }
}