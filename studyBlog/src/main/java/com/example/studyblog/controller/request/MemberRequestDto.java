package com.example.studyblog.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.studyblog.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

  @NotBlank
  private String email;

  @NotBlank
  private String nickname;

  @NotBlank
  @Size(min = 4, max = 32)
  @Pattern(regexp = "[a-z\\d]*${3,32}")
  private String password;

  private Gender gender;
}
