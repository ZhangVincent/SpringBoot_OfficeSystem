package com.zkp.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtHelper {

    // 有效时间
//    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    // 签名加密的密钥
    private static String tokenSignKey = "123456";

    // 根据输入生成token
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                // 分类
                .setSubject("AUTH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                // 设置主体部分
                .claim("userId", userId)
                .claim("username", username)
                // 签名部分
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    // 从token中获取用户Id
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;

            // 根据密钥解密token
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 从token中获取用户名称
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 如果运行失败缺少StringUtils依赖，需要修改pom中对应依赖的作用域
    public static void main(String[] args) {
        String token = JwtHelper.createToken(1L, "admin");
        System.out.println(token);
        System.out.println(JwtHelper.createToken(2L,"wjl"));
        System.out.println(JwtHelper.createToken(3L,"lrsjl"));
        System.out.println(JwtHelper.createToken(4L,"lisi"));
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUsername(token));
        // 输出
        // eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNjIzNDUx1VEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQDtHKfYQgAAAA.3ZDQJwsa_5AEUA_WeRl-DWG9Nj_gCi8Cingb1aGf8w54xpIah_DOOIqJqacM9RAQ1z8AgoaLYFo9842phcqvzg
        // 1
        // admin
        /**
         * eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQAKgKi4QgAAAA.0rJIdTc-BqNo1DYvuGhH_RkEhhTOXQE8g5pzW1EB2lhxhf4epyEStFLHFaXqyx2bICmyRCAeQJxlbu0D0MkKNA
         * eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kxRsjKCMPMSc1OBWsqzcpRqAY9PE6BAAAAA.fZPxJtIYhuerVYxWydx-Tw6adO45EpDA9FutGFfA1krTPgFjePcyy-OtPBYH_qGwaAmixZExX6MhX7qDWwHEmQ
         * eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kxRsjKGMPMSc1OBWnKKirNylGoBUlPVJkIAAAA.ISNb4ZT9IwhLmx25uZ9zDVtFIec8TLoVQ2tyNYI_ibEhlr6DcU6kTsPTCd29em_iu1gjB0M5S7Fo9_t7fb9UPw
         * eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kxRsoIy8xJzU4FacjKLM5VqAdweFWJBAAAA.osPXengmcMnSq9x8csTG5ISFP3lTak5xZyVlAXQLO7q9hva8x2TeuLT2dpIr3jT0vdjepJqLCCsGEKVxrS1SgQ
         */
    }
}
