package com.iscas.workingdiarys.security.filter;

import com.iscas.workingdiarys.exception.handler.JWTTokenExceptionHandler;
import com.iscas.workingdiarys.service.CustomUserDetailsService;
import com.iscas.workingdiarys.util.jjwt.JWTTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:    JWT过滤器配置类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/21
 * @Version:        1.0
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            if (!authHeader.startsWith("Bearer ")) {
                JWTTokenExceptionHandler.invalidAuthFormatException(response);
                return;
            }
            try {
                String username = JWTTokenUtil.parseToken(authHeader).getSubject();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException e) {
                JWTTokenExceptionHandler.expiredAuthorizationException(response);
                return;
            } catch (SignatureException e){
                JWTTokenExceptionHandler.invalidAuthorizationException(response);
            } catch (MalformedJwtException e){
                JWTTokenExceptionHandler.invalidAuthorizationException(response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
