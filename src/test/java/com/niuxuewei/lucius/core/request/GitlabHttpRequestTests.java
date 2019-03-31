package com.niuxuewei.lucius.core.request;

import com.niuxuewei.lucius.core.conf.SpringSecurityTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringSecurityTestConfig.class})
@PropertySource("classpath:lucius-config.properties")
public class GitlabHttpRequestTests {

    @Resource
    private GitlabHttpRequest gitlabHttpRequest;

    @Value("${lucius.gitlab.host.api.prefix}")
    private String GITLAB_HOST_API_PREFIX;

    @Test
    @WithUserDetails(value = "xavierniu", userDetailsServiceBeanName = "userDetailsService")
    public void getTest() {
        String jsonString = gitlabHttpRequest.get(GITLAB_HOST_API_PREFIX + "/users");
        System.out.println(jsonString);
    }

}
