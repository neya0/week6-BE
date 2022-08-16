package com.example.studyblog.service;

import com.example.studyblog.controller.request.*;
import com.example.studyblog.controller.response.*;
import com.example.studyblog.domain.Member;
import com.example.studyblog.jwt.TokenProvider;
import com.example.studyblog.repository.MemberRepository;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider){
    this.memberRepository = memberRepository;
    this. passwordEncoder = passwordEncoder;
    this.tokenProvider = tokenProvider;
  }

  @Transactional
  public ResponseDto<?> createMember(MemberRequestDto requestDto) {

    Optional<Member> memberIdExist = memberRepository.findByEmail(requestDto.getEmail());
    Optional<Member> nicknameExist = memberRepository.findByNickname(requestDto.getNickname());

    if (memberIdExist.isPresent()) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
              "이미 존재하는 아이디 입니다.");
    }
    if (nicknameExist.isPresent()) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
              "이미 존재하는 닉네임 입니다.");
    }

    Member member = Member.builder()
            .email(requestDto.getEmail())
            .nickname(requestDto.getNickname())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .gender(requestDto.getGender())
            .build();
    memberRepository.save(member);

    return ResponseDto.success(
        MemberResponseDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedAt())
            .modifiedAt(member.getModifiedAt())
            .build()
    );
  }

  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {

    Optional<Member> optionalMember = memberRepository.findByEmail(requestDto.getEmail());
    if (optionalMember.isEmpty()) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "사용자를 찾을 수 없습니다.");
    }

    Member member = optionalMember.get();

    if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
      return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
    }

    //토큰 만들기
    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
    tokenToHeaders(tokenDto, response);

    //멤버 리스폰스
    return ResponseDto.success(
        MemberResponseDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedAt())
            .modifiedAt(member.getModifiedAt())
            .build()
    );
  }

  public ResponseDto<?> logout(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    Member member = tokenProvider.getMemberFromAuthentication();
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "사용자를 찾을 수 없습니다.");
    }

    return tokenProvider.deleteRefreshToken(member);
  }

  public ResponseDto<?> checkNickname(NicknameRequestDto nickname){

    if (null != isPresentMember(nickname.getNickname())) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
              "이미 존재하는 닉네임 입니다.");
    }

    return ResponseDto.success("사용할 수 있는 nickname 입니다.");
  }

  public ResponseDto<?> checkEmail(MemberIdRequestDto memberId){
    Optional<Member> optionalMember = memberRepository.findByEmail(memberId.getEmail());

    if (optionalMember.isPresent()) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
              "이미 존재하는 아이디 입니다.");
    }

    return ResponseDto.success("사용할 수 있는 아이디 입니다.");
  }
  

  @Transactional(readOnly = true)
  public Member isPresentMember(String nickname) {
    Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
    return optionalMember.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

}
