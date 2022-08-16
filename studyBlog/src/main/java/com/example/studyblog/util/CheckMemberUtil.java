package com.example.studyblog.util;

import com.example.studyblog.domain.Member;
import com.example.studyblog.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class CheckMemberUtil {

    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
