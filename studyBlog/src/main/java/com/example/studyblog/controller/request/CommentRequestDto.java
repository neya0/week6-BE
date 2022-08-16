package com.example.studyblog.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentRequestDto {
    private String content;
}
