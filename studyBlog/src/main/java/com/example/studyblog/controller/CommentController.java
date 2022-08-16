package com.example.studyblog.controller;

import com.example.studyblog.controller.request.CommentRequestDto;
import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId, CommentRequestDto commentRequestDto, HttpServletRequest request){
        return commentService.createComment(postId,commentRequestDto,request);
    }

    @PutMapping("/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest request){
        return commentService.updateComment(commentId,commentRequestDto,request);
    }

    @DeleteMapping("/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request){
        return commentService.deleteComment(commentId, request);
    }

    @GetMapping("/{postId}")
    public ResponseDto<?> getComment(@RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
                                     @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                     @PathVariable Long postId){
        return commentService.getComment(postId, pageSize, page);
    }


}
