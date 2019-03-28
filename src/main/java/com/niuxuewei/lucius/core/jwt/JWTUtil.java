package com.niuxuewei.lucius.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@PropertySource("classpath:lucius-config.properties")
public class JWTUtil {

    private static Long EXPIRE_TIME;

    /**
     * 注入JWT默认过期时间，单位是分钟
     *
     * @param time 在lucius-config.properties中配置jwt.expire-time
     */
    @Value("${jwt.expire-time}")
    public void setExpireTime(String time) {
        Long exTime = Long.valueOf(time);
        EXPIRE_TIME = exTime * 60 * 1000;
    }

    /**
     * 校验token是否正确
     *
     * @param token    token
     * @param username 用户名
     * @param secret   密码
     * @return 验证成功与否
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 无需secret也可以获取的信息
     *
     * @param token token
     * @return 用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param username 用户名
     * @param secret   密码
     * @return 加密过的token
     */
    public static String sign(String username, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

}
