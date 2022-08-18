package com.example.studyblog.service;

import com.example.studyblog.controller.request.CommentRequestDto;
import com.example.studyblog.controller.response.CommentResponseDto;
import com.example.studyblog.controller.response.MsgResponseDto;
import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.domain.Comment;
import com.example.studyblog.domain.Member;
import com.example.studyblog.domain.Post;
import com.example.studyblog.jwt.TokenProvider;
import com.example.studyblog.repository.CommentRepository;
import com.example.studyblog.repository.PostRepository;
import lombok.*;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Builder
@RequiredArgsConstructor
public class CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<CommentResponseDto> createComment(Long postId, CommentRequestDto commentRequestDto, Member member){
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
            return ResponseDto.fail("POST_NOT_FOUND", "해당 게시글을 찾을 수 없습니다.");
        }
        
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(commentRequestDto.getContent())
                .build();
        System.out.println("commentRequestDto.getContent() = " + commentRequestDto.getContent());
        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .nickname(member.getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
        return ResponseDto.success(commentResponseDto);
    }

    @Transactional
    public ResponseDto<?> createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest request){
        Optional<ResponseDto<?>> validationResult = validationToken(request);
        if (validationResult.isPresent()){
            return validationResult.get();
        }else{
            Member member = tokenProvider.getMemberFromAuthentication();
            return createComment(postId, commentRequestDto, member);
        }
    }

    @Transactional
    public ResponseDto<MsgResponseDto> deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment==null){
            return ResponseDto.fail("COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다.");
        }else {
            commentRepository.delete(comment);
            return ResponseDto.success(new MsgResponseDto("댓글을 삭제했습니다."));
        }

    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest request){
        Optional<ResponseDto<?>> validationResult = validationToken(request);
        if (validationResult.isPresent()){
            return validationResult.get();
        }else {
            return deleteComment(commentId);
        }
    }

    @Transactional
    public ResponseDto<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment==null){
            return ResponseDto.fail("COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다.");
        }else {
            comment.update(commentRequestDto.getContent());
            System.out.println("comment = " + comment);
            return ResponseDto.success(CommentResponseDto.builder()
                            .modifiedAt(comment.getModifiedAt())
                            .createdAt(comment.getCreatedAt())
                            .content(comment.getContent())
                            .nickname(comment.getMember().getNickname())
                            .id(commentId)
                    .build());
        }
    }

    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request){
        Optional<ResponseDto<?>> validationResult = validationToken(request);
        if (validationResult.isPresent()){
            return validationResult.get();
        }else {
            return updateComment(commentId, commentRequestDto);
        }
    }

    @Transactional(readOnly = true)
    public ResponseDto<List<CommentResponseDto>> getComment(Long postId, int pageSize, int page){
        Post post = postRepository.findById(postId).orElse(null);
        if (post==null){
            return ResponseDto.fail("POST_NOT_FOUND", "댓글을 찾을 수 없습니다.");
        }else {
            PageRequest pageRequest = PageRequest.of(page, pageSize);
            List<Comment> commentList = commentRepository.findAllByPost(post, pageRequest);
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
            for (Comment comment : commentList) {
                CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                        .content(comment.getContent())
                        .id(comment.getId())
                        .nickname(comment.getMember().getNickname())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build();
                commentResponseDtoList.add(commentResponseDto);
            }
            return ResponseDto.success(commentResponseDtoList);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ResponseDto<?>> validationToken(HttpServletRequest request){
        if (null == request.getHeader("RefreshToken")) {
            return Optional.of(ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다."));
        }

        if (null == request.getHeader("Authorization")) {
            return Optional.of(ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다."));
        }

        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return Optional.of(ResponseDto.fail("INVALID_TOKEN",
                    "Token이 유효하지 않습니다."));
        }
        return Optional.empty();
    }
}
