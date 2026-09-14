package com.ticket.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            if (jwtProvider.validateToken(token)) {

                //에러 찾기용 임시 출력문(지울것)
                System.out.println("받은 토큰 = " + token);
                System.out.println("토큰 유효 = " + jwtProvider.validateToken(token));

                //userid 랑 role 가져오기
                Long userId = jwtProvider.getUserId(token);
                String role = jwtProvider.getRole(token);

                //테스트 (지울것)
                System.out.println("userId = " + userId);
                System.out.println("role = " + role);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                //인증 객체 생성
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(authority)
                        );

                //인증된 사용자의 요청을 Spring Security에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
