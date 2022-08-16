package com.example.studyblog.controller.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
  private Long id;
  private String email;
  private String nickname;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
