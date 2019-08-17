package org.igetwell.system.security;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
public class JwtTokenUtils {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse
     * @return the Claims object extracted from specified token or null if a token is invalid.
     */
    public Claims parseToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .requireIssuer(issuer)
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e){
            claims = null;
        }

        return claims;
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param subject the user for which the token will be generated
     * @return the JWT token
     */
    public String createToken(String subject) {
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", SignatureAlgorithm.RS256.getValue())
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(createExpirationDate())
                .compressWith(CompressionCodecs.GZIP)
                .signWith(HS512, secret);

        return builder.compact();

    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public String getUsername(String token) {
        String username;
        try {
            final Claims claims = parseToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    /**
     * 生成过期时间
     * @return
     */
    public Date createExpirationDate(){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 解析Token获取过期时间
     * @param token
     * @return
     */
    public Date getExpirationDate(String token) {
        Date expiration;
        try {
            final Claims claims = parseToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration  = null;
        }
        return expiration;
    }

    /**
     * 验证Token是否过期
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    /**
     * 解析Token验证用户是否当前用户
     * @param token
     * @param info
     * @return
     */
    public Boolean validateToken(String token, UserDetails info) {
        final Claims claims = parseToken(token);
        final String username = claims.getSubject();
        return (username.equals(info.getUsername()) && !isTokenExpired(token));
    }

    /**
     *
     * </p>生成密钥</p>
     * @param base64Key base64编码密钥
     * @return
     * @date 2017年7月8日
     */
    private SecretKey generalKey(String base64Key) {
        byte[] secretBytes = Base64.decodeBase64(base64Key);
        SecretKey key = new SecretKeySpec(secretBytes, SignatureAlgorithm.RS256.getJcaName());
        return key;
    }

    public static void main(String [] args){
        System.err.println("test1:" +System.currentTimeMillis());
        System.err.println("test2:" +(System.currentTimeMillis() + 180 * 1000));
        Date date = new Date(System.currentTimeMillis() + 180 * 1000);
        JwtTokenUtils jwtTokenUtil = new JwtTokenUtils();

        System.out.println("*************************************");
        String token = jwtTokenUtil.createToken("admin");
        System.err.println(token);
        System.out.println("*************************************");

        System.err.println("parse: " + jwtTokenUtil.parseToken(token));
        System.err.println("ExpirationDate: "+ jwtTokenUtil.getExpirationDate(token));
        System.err.println("Invalid " + jwtTokenUtil.isTokenExpired(token));
    }
}
