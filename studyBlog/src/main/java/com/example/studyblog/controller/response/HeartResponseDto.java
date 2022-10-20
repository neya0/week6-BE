package com.example.studyblog.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class HeartResponseDto {
    private boolean isHeart;
    private Long postHeartCount;
}
