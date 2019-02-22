package com.iscas.workingdiarys.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description:    基于数据库的RBAC权限控制
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/25
 * @Version:        1.0
 */
@Component("rbacauthorityservice")
public class RbacAuthorityService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object userInfo = authentication.getPrincipal();

        boolean hasPermission  = false;

        if (userInfo instanceof UserDetails) {

            String username = ((UserDetails) userInfo).getUsername();

            //获取资源
            Set<String> urls = new HashSet();
            urls.add("/user/**"); // 这些 url 都是要登录后才能访问，且其他的 url 都不能访问！
            urls.add("/admin/**");
            urls.add("/cert/**");
            urls.add("/authorize/**");
            urls.add("/sse/**");
            AntPathMatcher antPathMatcher = new AntPathMatcher();

            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
            return hasPermission;
        } else {
            return false;
        }
    }
}
