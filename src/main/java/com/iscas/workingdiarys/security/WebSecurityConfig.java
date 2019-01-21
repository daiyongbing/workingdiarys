package com.iscas.workingdiarys.security;

import com.iscas.workingdiarys.security.filter.JWTAuthenticationFilter;
import com.iscas.workingdiarys.security.filter.JWTLoginFilter;
import com.iscas.workingdiarys.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Description:    Spring Security配置类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/18
 * @Version:        1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    JWTLoginFilter jwtLoginFilter(AuthenticationManager authenticationManager){
        return new JWTLoginFilter(authenticationManager);
    }

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationTokenFilter; // JWT 拦截器

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用CSRF
        http.csrf().disable()
                // 使用JWT，关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 开启http登录验证
                //.httpBasic()//.authenticationEntryPoint(authenticationEntryPoint)

                //.and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll() //开放注册接口
                .antMatchers(HttpMethod.GET, "/user/checkname","/user/checkid").permitAll() //开放验证接口
                .antMatchers(HttpMethod.GET, "/user/test1","/user/test2").permitAll() //开放验证接口
                .antMatchers("/admin").hasRole("ADMIN") // 只有管理员能访问/admin/**
                .anyRequest()
                .access("@rbacauthorityservice.hasPermission(request,authentication)") // RBAC 动态 url 认证
                //.authenticated() //所有接口都必须经过身份验证

                .and()
                .addFilter(jwtLoginFilter(authenticationManager()))
                //.addFilterAfter(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
