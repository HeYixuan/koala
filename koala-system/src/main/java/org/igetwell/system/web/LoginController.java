package org.igetwell.system.web;


import lombok.AllArgsConstructor;
import org.igetwell.common.constans.cache.CacheKey;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.security.SpringSecurityUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class LoginController {

    private final RedisTemplate redisTemplate;
    private final TokenStore tokenStore;
    private final CacheManager cacheManager;
    private final AuthorizationServerEndpointsConfiguration endpoints;
    private final ClientDetailsService clientDetailsService;


    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
     * @param username 登录名
     * @param password 登录密码
     * @return access_token
     */
    /**
     * 登录获取用户访问令牌，基于oauth2密码模式登录,无需签名,返回access_token
     */
    @PostMapping("/login/token")
    public ResponseEntity<OAuth2AccessToken> getLoginToken(@RequestParam String username, @RequestParam String password) throws Exception {
        OAuth2AccessToken result = getToken(username, password);
        return new ResponseEntity<>(result);
    }

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 确认授权页
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public String confirm_access(HttpServletRequest request, HttpSession session, Map model) {
        Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
        model.put("scopeList", scopeList.keySet());
        Object auth = session.getAttribute("authorizationRequest");
        if (auth != null) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            model.put("app", clientDetails.getAdditionalInformation());
            model.put("user", SpringSecurityUtils.getUser());
        }
        return "confirm_access";
    }

    /**
     * 退出token
     *
     */
    @DeleteMapping("/logout/token")
    public ResponseEntity logout(String token) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken == null || StringUtils.isEmpty(accessToken.getValue())) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"退出失败，token 无效");
        }

        OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);
        // 清空用户信息
        cacheManager.getCache(CacheKey.USER_DETAILS)
                .evict(auth2Authentication.getName());
        // 清空access token
        tokenStore.removeAccessToken(accessToken);
        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        return new ResponseEntity();
    }

    /**
     * 退出移除令牌
     *
     * @param token
     */
    @PostMapping("/logout/removeToken")
    public ResponseEntity removeToken(@RequestParam String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return new ResponseEntity();
    }

    /**
     * 生成 oauth2 token
     *
     * @param userName
     * @param password
     * @return
     */
    private OAuth2AccessToken getToken(String userName, String password) throws Exception {
        // 使用oauth2密码模式登录.
        Map<String, String> postParameters = new HashMap<>();
        postParameters.put("username", userName);
        postParameters.put("password", password);
        postParameters.put("client_id", "test");
        postParameters.put("client_secret", "test");
        postParameters.put("grant_type", "password");
        return createAccessToken(endpoints,postParameters);
    }

    /**
     * 认证服务器原始方式创建AccessToken
     * @param endpoints
     * @param postParameters
     * @return
     * @throws Exception
     */
    private static OAuth2AccessToken createAccessToken(AuthorizationServerEndpointsConfiguration endpoints, Map<String, String> postParameters) throws Exception {
        Assert.notNull(postParameters.get("client_id"),"client_id not null");
        Assert.notNull(postParameters.get("client_secret"),"client_secret not null");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(postParameters.get("client_id"),postParameters.get("client_secret"), Collections.emptyList());
        org.springframework.http.ResponseEntity<OAuth2AccessToken> responseEntity = endpoints.tokenEndpoint().postAccessToken(auth,postParameters);
        return  responseEntity.getBody();
    }
}
