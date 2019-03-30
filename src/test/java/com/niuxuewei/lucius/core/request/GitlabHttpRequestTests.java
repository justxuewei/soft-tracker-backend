package com.niuxuewei.lucius.core.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitlabHttpRequestTests {

    @Resource
    private GitlabHttpRequest gitlabHttpRequest;

    private static final Integer USER_ID = 1;

    @Before
    public void before() {
        gitlabHttpRequest.deleteFromRedis(USER_ID);
    }

    @Test
    public void redisTest() {
        String accessToken = gitlabHttpRequest.getAccessTokenFromRedis(USER_ID);
        Assert.assertNull(accessToken);
        gitlabHttpRequest.setAccessTokenIntoRedis(USER_ID, "asdafwrew", 100);
        accessToken = gitlabHttpRequest.getAccessTokenFromRedis(USER_ID);
        Assert.assertEquals("asdafwrew", accessToken);
    }

}
