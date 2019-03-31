package com.niuxuewei.lucius.core.conf;

import com.niuxuewei.lucius.entity.po.UserPO;
import com.niuxuewei.lucius.entity.security.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        GrantedAuthority authority = new SimpleGrantedAuthority("admin");
        UserPO userPO = new UserPO();
        userPO.setId(1);
        userPO.setUsername("xavierniu");
        userPO.setPassword("1234567");
        UserDetailsImpl userDetails = new UserDetailsImpl(userPO, Collections.singletonList(authority));
        return new InMemoryUserDetailsManager(Collections.singletonList(userDetails));
    }

}
