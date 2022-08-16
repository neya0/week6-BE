package com.example.studyblog.controller;

import com.example.studyblog.controller.request.LoginRequestDto;
import com.example.studyblog.controller.request.MemberIdRequestDto;
import com.example.studyblog.controller.request.MemberRequestDto;
import com.example.studyblog.controller.request.NicknameRequestDto;
import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MemberController {

  private final MemberService memberService;

  @Autowired
  MemberController(MemberService memberService){
    this.memberService = memberService;
  }

  @RequestMapping(value = "/api/member/signup", method = RequestMethod.POST)
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }

  @PostMapping("/api/member/exist/nickname")
  public ResponseDto<?> checkNickname(@RequestBody NicknameRequestDto nickname){
    return memberService.checkNickname(nickname);
  }

  @PostMapping("/api/member/exist/memberId")
  public ResponseDto<?> checkMemberId(@RequestBody MemberIdRequestDto memberId){
    return memberService.checkEmail(memberId);
  }

  @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
  public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }

//  @RequestMapping(value = "/api/auth/member/reissue", method = RequestMethod.POST)
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    return memberService.reissue(request, response);
//  }

  @RequestMapping(value = "/api/auth/member/logout", method = RequestMethod.POST)
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }
}
