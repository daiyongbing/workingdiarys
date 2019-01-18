package com.iscas.workingdiarys.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @Description:    Spring Security配置类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/18
 * @Version:        1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用CSRF
        http.csrf().disable()
                // 关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll() //开放注册接口
                .antMatchers(HttpMethod.GET, "/user/checkname","/user/checkid").permitAll() //开放验证接口
                .antMatchers("/admin").hasRole("ADMIN") // 只有管理员能访问/admin/**
                .anyRequest()
                .authenticated(); //所有接口都必须经过身份验证


    }
}
