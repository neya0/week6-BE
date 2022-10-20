package com.example.studyblog.shared;

import com.example.studyblog.controller.response.ResponseDto;
import com.example.studyblog.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class TokenValidator {
    @Autowired
    private TokenProvider tokenProvider;

    public Optional<ResponseDto<?>> validationToken(HttpServletRequest request){
        if (null == request.getHeader("RefreshToken")) {
            return Optional.of(ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다."));
        }

        if (null == request.getHeader("Authorization")) {
            return Optional.of(ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다."));
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return Optional.of(ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다."));
        }
        return Optional.empty();
    }
}
