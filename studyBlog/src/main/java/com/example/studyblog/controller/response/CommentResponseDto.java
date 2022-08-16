package com.example.studyblog.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String content;
    private Date createdAt;
    private Date modifiedAt;
}
