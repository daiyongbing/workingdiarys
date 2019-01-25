package com.iscas.workingdiarys.service;

import com.iscas.workingdiarys.entity.CustomUserDetails;
import com.iscas.workingdiarys.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      /*  CustomUserDetails userInfo = new CustomUserDetails();
        userInfo.setUsername(username);
        userInfo.setPassword(new BCryptPasswordEncoder().encode("123"));

        Set authoritiesSet = new HashSet();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authoritiesSet.add(authority);
        userInfo.setAuthorities(authoritiesSet);
        return userInfo;*/

        if (username == null || "".equals(username)){
            return null;
        }
        CustomUserDetails userDetails = userMapper.findByUserName(username);
        if(userDetails == null){
            throw new UsernameNotFoundException(username);
        }
        String password = userDetails.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.info(username+":"+authorities);
        //return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(userDetails.getAuthorities()));
        return new User(username, password, authorities);
    }
}
