package com.niuxuewei.lucius.core.request;

import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.utils.RedisUtils;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import com.niuxuewei.lucius.entity.po.Gitlab;
import com.niuxuewei.lucius.mapper.GitlabMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.Resource;
import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Slf4j
@PropertySource(value = "classpath:lucius-config.properties")
public class GitlabHttpRequest extends HttpRequest {

    private static final String CACHE_KEY_PREFIX = "gitlab-private-token@";

    private static final String ADMIN_ACCESS_TOKEN_CACHE_KEY = "gitlab-admin-access-token";

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private GitlabMapper gitlabMapper;

    @Value("${lucius.gitlab.oauth.url}")
    private String GITLAB_OAUTH_URL;

    @Value("${lucius.gitlab.admin.username}")
    private String GITLAB_ADMIN_USERNAME;

    @Value("${lucius.gitlab.admin.password}")
    private String GITLAB_ADMIN_PASSWORD;

    /**
     * 获取存放gitlab access token的key
     */
    public String getCacheKey(Integer userId) {
        return CACHE_KEY_PREFIX + userId;
    }

    /**
     * 从Cache中获取access token
     *
     * @param userId 当前登录用户ID
     * @return 如果存在则返回access token, 如果不存在则返回null
     */
    public String getPrivateTokenFromRedis(Integer userId) {
        if (!redisUtils.hasKey(getCacheKey(userId))) {
            log.debug("access token不存在");
            return null;
        }
        return (String) redisUtils.get(getCacheKey(userId));
    }

    /**
     * 创建一个privateToken
     */
    public String createPrivateToken(Integer userId) {
        String accessToken;
        // 从redis找admin的access token
        if (redisUtils.hasKey(ADMIN_ACCESS_TOKEN_CACHE_KEY)) {
            log.debug("redis中保存了admin的access token");
            accessToken = (String) redisUtils.get(ADMIN_ACCESS_TOKEN_CACHE_KEY);
        } else {
            log.debug(GITLAB_OAUTH_URL);
            JSONObject data = post(GITLAB_OAUTH_URL, null, new LinkedMultiValueMap<String, String>() {{
                add("grant_type", "password");
                add("username", GITLAB_ADMIN_USERNAME);
                add("password", GITLAB_ADMIN_PASSWORD);
            }});
            log.debug("gitlab返回的数据为: {}", data);
        }
        return null;
    }

    /**
     * 向Cache中存放access token
     */
    public void setPrivateTokenIntoRedis(Integer userId, String accessToken, Integer expTime) {
        redisUtils.set(getCacheKey(userId), accessToken, expTime);
    }

    /**
     * 从cache中删除access token
     */
    public void deleteFromRedis(Integer userId) {
        redisUtils.del(getCacheKey(userId));
    }

    /**
     * 获取gitlab的accessToken
     */
    public String getPrivateToken() {
        Integer userId = SecurityUtils.getUser().getId();
        String accessToken = getPrivateTokenFromRedis(userId);

        // 如果没有缓存，那么去gitlab中获取，并保存到redis中
        if (accessToken == null) {
            // do something
        }

        return accessToken;
    }

}
