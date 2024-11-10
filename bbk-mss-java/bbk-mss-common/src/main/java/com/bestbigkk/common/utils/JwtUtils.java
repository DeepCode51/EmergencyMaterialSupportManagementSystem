package com.bestbigkk.common.utils;


import java.util.*;
import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

/**
* @author: 开
* @date: 2020-03-25 18:00:55
* @describe: Jwt
*/
@Component
public class JwtUtils {

    /** 生成签名是所使用的秘钥*/
    private final String encodedSecretKey = "123qwer";

    /**
     * 令牌有效期(s)
     */
    private Long expireSeconds  = 7200L;

    /**生成签名的时候所使用的加密算法*/
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    /**
     * 生成 JWT Token 字符串
     *
     * @param issue       签发人名称
     * @param claims    额外添加到荷部分的信息。
     *                  例如可以添加用户名、用户ID、用户（加密前的）密码等信息
     */
    public String encode(String issue, Map<String, Object> claims) {
        if (claims == null) {
            claims = new HashMap<>(2);
        }
        // 签发时间（iat）：荷载部分的标准字段之一
        Long now = System.currentTimeMillis();
        claims.put("iat", now);
        claims.put("exp", new Date(now + (expireSeconds = expireSeconds == null ? 3600L : expireSeconds) * 1000).getTime());

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                // 荷载部分的非标准字段/附加字段，一般写在标准的字段之前。
                .setClaims(claims)
                // JWT ID（jti）：荷载部分的标准字段之一，JWT 的唯一性标识，虽不强求，但尽量确保其唯一性。
                .setId(UUID.randomUUID().toString())
                // 签发人（issue）：荷载部分的标准字段之一，代表这个 JWT 的所有者。通常是 username、userid 这样具有用户代表性的内容。
                .setSubject(issue)
                // 设置生成签名的算法和秘钥
                .signWith(signatureAlgorithm, encodedSecretKey);

        return builder.compact();
    }


    /**
     * JWT Token 由 头部 荷载部 和 签名部 三部分组成。签名部分是由加密算法生成，无法反向解密。
     * 而 头部 和 荷载部分是由 Base64 编码算法生成，是可以反向反编码回原样的。
     * 这也是为什么不要在 JWT Token 中放敏感数据的原因。
     *
     * @param jwtToken 加密后的token
     * @return claims 返回荷载部分的键值对
     */
    public Claims decode(String jwtToken) {

        // 得到 DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(encodedSecretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * 判断一个token是否过期
     * @param jwtToken
     * @return
     */
    public boolean isExpired(String jwtToken) {
        try {
            final Claims decode = decode(jwtToken);
            long exp = (long) decode.get("exp");
            return System.currentTimeMillis() > exp;
        } catch (Exception e) {
            return true;
        }
    }

}
