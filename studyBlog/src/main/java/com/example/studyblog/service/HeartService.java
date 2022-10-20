package com.example.studyblog.service;

import com.example.studyblog.controller.response.HeartResponseDto;
import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.domain.Comment;
import com.example.studyblog.domain.Heart;
import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.Post;
import com.example.studyblog.jwt.TokenProvider;
import com.example.studyblog.repository.CommentRepository;
import com.example.studyblog.repository.HeartRepository;
import com.example.studyblog.repository.PostRepository;
import com.example.studyblog.shared.TokenValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HeartService {
    private HeartRepository heartRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private TokenProvider tokenProvider;
    private TokenValidator tokenValidator;

    public ResponseDto<?> HeartPost(Long postId, HttpServletRequest request) throws Exception {
        Optional<ResponseDto<?>> validationResult = tokenValidator.validationToken(request);
        if (validationResult.isPresent()){
            return validationResult.get();
        }else{
            Member member = tokenProvider.getMemberFromAuthentication();
            return HeartPost(postId, member);
        }
    }

    public ResponseDto<?> HeartComment(Long postId, HttpServletRequest request) throws Exception {
        Optional<ResponseDto<?>> validationResult = tokenValidator.validationToken(request);
        if (validationResult.isPresent()){
            return validationResult.get();
        }else{
            Member member = tokenProvider.getMemberFromAuthentication();
            return HeartComment(postId, member);
        }
    }

    public ResponseDto<HeartResponseDto> HeartPost(Long postId, Member member) throws Exception {
        return Heart(postId, member, HeartType.POST);
    }

    public ResponseDto<HeartResponseDto> HeartComment(Long postId, Member member) throws Exception {
        return Heart(postId, member, HeartType.COMMENT);
    }
    public ResponseDto<HeartResponseDto> Heart(Long id, Member member, HeartType heartType) throws Exception{
        Comment comment;
        Post post;
        if (heartType == HeartType.POST){
            post = postRepository.findById(id).orElse(null);
            comment = null;
            if (post ==null){
                return ResponseDto.fail("POST_NOT_FOUND","게시글을 찾을 수 없습니다.");
            }
        }else if(heartType == HeartType.COMMENT){
            comment = commentRepository.findById(id).orElse(null);
            post = comment.getPost();
            if (comment == null){
                return ResponseDto.fail("COMMENT_NOT_FOUND","댓글을 찾을 수 없습니다.");
            }
        }else{
            throw new IllegalArgumentException();
        }
        Heart.HeartId heartId = Heart.HeartId.builder()
                .post(post)
                .comment(comment)
                .member(member)
                .build();
        Heart heart = heartRepository.findById(heartId).orElse(null);
        // 좋아요
        if (heart ==null){
            heartRepository.save(new Heart(post, comment, member));
            Long heartCount = heartRepository.countHeartById_Post(post);
            HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                    .isHeart(true)
                    .postHeartCount(heartCount)
                    .build();
            return ResponseDto.success(heartResponseDto);
        }
        //좋아요 취소
        else{
            heartRepository.delete(new Heart(post, comment, member));
            Long heartCount = heartRepository.countHeartById_Post(post);
            HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                    .isHeart(false)
                    .postHeartCount(heartCount)
                    .build();
            return ResponseDto.success(heartResponseDto);
        }
    }
    public enum HeartType{POST, COMMENT}
}
