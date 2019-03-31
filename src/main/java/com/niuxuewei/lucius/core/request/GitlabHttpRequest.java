package com.niuxuewei.lucius.core.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niuxuewei.lucius.core.exception.NotExistedException;
import com.niuxuewei.lucius.core.utils.DateUtils;
import com.niuxuewei.lucius.core.utils.RedisUtils;
import com.niuxuewei.lucius.core.utils.SecurityUtils;
import com.niuxuewei.lucius.entity.po.GitlabUserPO;
import com.niuxuewei.lucius.mapper.GitlabUserPOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@PropertySource(value = "classpath:lucius-config.properties")
public class GitlabHttpRequest extends HttpRequest {

    private static final String IMPERSONATION_TOKEN_CACHE_KEY_PREFIX = "gitlab-impersonation-token@";

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private GitlabUserPOMapper gitlabMapper;

    @Value("${lucius.gitlab.host.api.prefix}")
    private String GITLAB_HOST_API_PREFIX;

    @Value("${lucius.gitlab.oauth.admin.access-token}")
    private String GITLAB_OAUTH_ADMIN_ACCESS_TOKEN;

    @Value("${lucius.gitlab.impersonation-token.expired}")
    private Integer GITLAB_IMPERSONATION_TOKEN_EXPIRED;

    /**
     * 获取存放gitlab access token的key
     */
    private String getImpersonationTokenCacheKey(Integer userId) {
        return IMPERSONATION_TOKEN_CACHE_KEY_PREFIX + userId;
    }

    /**
     * 从Cache中获取access token
     *
     * @param userId 当前登录用户ID
     * @return 如果存在则返回access token, 如果不存在则返回null
     */
    private String getImpersonationTokenFromRedis(Integer userId) {
        if (!redisUtils.hasKey(getImpersonationTokenCacheKey(userId))) {
            log.debug("impersonation token不存在, cache key: {}", getImpersonationTokenCacheKey(userId));
            return null;
        }
        return (String) redisUtils.get(getImpersonationTokenCacheKey(userId));
    }

    /**
     * 从gitlab中创建一个ImpersonationToken
     */
    private String createImpersonationToken(Integer userId) {
        GitlabUserPO gitlabUserPO = gitlabMapper.selectFirstByUserId(userId);
        if (gitlabUserPO == null) throw new NotExistedException("gitlab账号不存在");
        String createImpersonationTokenUrl = GITLAB_HOST_API_PREFIX + "/users/" + gitlabUserPO.getGitlabId() + "/impersonation_tokens";
        // 秒转换为天
        Date expiredDate = DateUtils.addDay(new Date(), GITLAB_IMPERSONATION_TOKEN_EXPIRED / 24 / 60);
        JSONObject impersonationTokenData = JSON.parseObject(post(GitlabHttpRequestAuthMode.ADMIN_AUTH,
                createImpersonationTokenUrl, null, new LinkedMultiValueMap<String, String>() {{
                    add("name", "lucius");
                    add("expires_at", DateUtils.formatData(expiredDate, "yyyy-MM-dd"));
                    add("scopes[]", "api");
                }}));
        log.debug("获取Impersonation Token的返回数据为: {}", impersonationTokenData);
        return impersonationTokenData.getString("token");
    }

    /**
     * 获取gitlab的impersonationToken
     * 如果在缓存中不存在，则从gitlab中创建一个并存入redis
     */
    private String getImpersonationToken() {
        Integer userId = SecurityUtils.getUserId();
        String impersonationToken = getImpersonationTokenFromRedis(userId);

        // 如果没有缓存，那么去gitlab中获取，并保存到redis中
        if (impersonationToken == null) {
            impersonationToken = createImpersonationToken(userId);
            // 保存到redis
            redisUtils.set(getImpersonationTokenCacheKey(userId), impersonationToken, GITLAB_IMPERSONATION_TOKEN_EXPIRED - 100);
        }
        return impersonationToken;
    }

    /**
     * 通用请求
     *
     * @param auth       是否需要开启验证
     * @param url        请求地址，不需要添加GITLAB_HOST_API_PREFIX前缀
     * @param headersMap headersMap headers，用HashMap<String, String>
     * @param dataMap    dataMap from表单，用LinkedMultiValueMap
     * @param method     method 请求方法
     * @return json字符串
     */
    public String request(GitlabHttpRequestAuthMode auth, String url, Map<String, String> headersMap, MultiValueMap<String, String> dataMap, HttpMethod method) {
        if (headersMap == null) {
            headersMap = new HashMap<>();
        }
        if (auth == GitlabHttpRequestAuthMode.ADMIN_AUTH) {
            headersMap.put("Authorization", "Bearer " + GITLAB_OAUTH_ADMIN_ACCESS_TOKEN);
        } else if (auth == GitlabHttpRequestAuthMode.USER_AUTH) {
            headersMap.put("Private-Token", getImpersonationToken());
        }
        return super.request(GITLAB_HOST_API_PREFIX + url, headersMap, dataMap, method);
    }

    /**
     * 任何request都讲添加当前登录用户的Private Token
     */
    public String request(GitlabHttpRequestAuthMode auth, String url, MultiValueMap<String, String> dataMap, HttpMethod method) {
        return request(auth, url, null, dataMap, method);
    }

    public String post(GitlabHttpRequestAuthMode auth, String url, Map<String, String> headersMap, MultiValueMap<String, String> dataMap) {
        return request(auth, url, headersMap, dataMap, HttpMethod.POST);
    }

    public String post(GitlabHttpRequestAuthMode auth, String url, MultiValueMap<String, String> dataMap) {
        return post(auth, url, null, dataMap);
    }
}
