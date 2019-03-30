package com.niuxuewei.lucius.core.request;

import com.niuxuewei.lucius.core.utils.RedisUtils;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Component
@Slf4j
public class GitlabHttpRequest extends HttpRequest {

    private static String CACHE_PREFIX = "gitlab-access-token@";

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取存放gitlab access token的key
     */
    public String getCacheKey(Integer userId) {
        return CACHE_PREFIX + userId;
    }

    /**
     * 从Cache中获取access token
     *
     * @param userId 当前登录用户ID
     * @return 如果存在则返回access token, 如果不存在则返回null
     */
    public String getAccessTokenFromRedis(Integer userId) {
        if (!redisUtils.hasKey(getCacheKey(userId))) {
            log.debug("access token不存在");
            return null;
        }
        return (String) redisUtils.get(getCacheKey(userId));
    }

    /**
     * 向Cache中存放access token
     */
    public void setAccessTokenIntoRedis(Integer userId, String accessToken, Integer expTime) {
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
     * 1. 从redis中查询是否有缓存的accessToken
     */
    public String getAccessToken() {
        Integer userId = SecurityUtils.getUser().getId();
        String accessToken = getAccessTokenFromRedis(userId);

        // 如果没有缓存，那么去gitlab中获取，并保存到redis中
        if (accessToken == null) {
            // do something
        }

        return accessToken;
    }

}
