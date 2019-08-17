package org.igetwell.system.security.extractor;

import org.igetwell.system.security.JwtTokenUtils;
import org.igetwell.system.security.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TOKEN 认证过滤器
 */
@Component
public class JwtTokenExtractor extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private SpringSecurityService springSecurityService;

    private final static String AUTHORIZATION = "Authorization";
    private final static String AUTHORIZATION_HEADER = "X-API-TOKEN";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final static String VERSION_HEADER = "X-Version";
    private final static String DEVICE_HEADER = "X-Device";


    protected String extractToken(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(AUTHORIZATION);
        if (StringUtils.isEmpty(header) || !header.startsWith(TOKEN_PREFIX)){
            logger.error("Request header X-API-TOKEN is empty");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return null;
        }

        final String token = header.substring(7);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

    protected void extractHeaderToken(HttpServletRequest request, HttpServletResponse response) {
        String version = request.getHeader(VERSION_HEADER);
        String device = request.getHeader(DEVICE_HEADER);
        // 校验平台和接口版本号，校验平台取值
        if(StringUtils.isEmpty(version) || StringUtils.isEmpty(device)){
            logger.error("Request header X-Version or X-Device is empty");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        extractHeaderToken(request, response);
        String token = extractToken(request, response);
        if (!StringUtils.hasText(token.trim()) && null == SecurityContextHolder.getContext().getAuthentication()) {
            String username = jwtTokenUtils.getUsername(token);
            UserDetails systemUserDetails =  springSecurityService.loadUserByUsername(username);
            if (jwtTokenUtils.validateToken(token, systemUserDetails)) {
                UsernamePasswordAuthenticationToken  authentication = new UsernamePasswordAuthenticationToken(
                        systemUserDetails, null, systemUserDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else{
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print("Invalid token error.");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
