package com.example.studyblog.controller;

import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.service.HeartService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/like")
public class HeartController {
    private HeartService heartService;

    @PostMapping("/post/{postId}")
    public ResponseDto<?> HeartPost(@PathVariable Long postId, HttpServletRequest request) throws Exception{
        return heartService.HeartPost(postId, request);
    }

    @PostMapping("/comment/{commentId}")
    public ResponseDto<?> HeartComment(@PathVariable Long commentId, HttpServletRequest request) throws Exception{
        return heartService.HeartComment(commentId, request);
    }
}
