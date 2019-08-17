package org.igetwell.system.security.extractor;

import org.apache.commons.lang.StringUtils;
import org.igetwell.system.security.JwtTokenUtils;
import org.igetwell.system.security.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TOKEN 认证过滤器
 */
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


    /*protected String extractToken(HttpServletRequest request, HttpServletResponse response) {
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
        if (!StringUtils.isEmpty(token.trim()) && null == SecurityContextHolder.getContext().getAuthentication()) {
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
    }*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filtering B on...........................................................");
        String version = request.getHeader(VERSION_HEADER);
        String device = request.getHeader(DEVICE_HEADER);
        String header = request.getHeader(AUTHORIZATION_HEADER);
        String uri = request.getRequestURI();

        // 不过滤的请求链接
        /*boolean permit = false || uri.startsWith("/oauth/token");

        if (permit) {
            filterChain.doFilter(request, response);
            return;
        }*/

        if (StringUtils.isBlank(header) || !header.startsWith(TOKEN_PREFIX)){
            logger.error("Request header X-API-TOKEN is empty");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        // 校验平台和接口版本号，校验平台取值
        /*if(StringUtils.isBlank(version) || StringUtils.isBlank(device)){
            logger.error("Request header X-Version or X-Device is empty");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }*/

        final String token = header.substring(7);
        String username = jwtTokenUtils.getUsername(token);
        if (!StringUtils.isBlank(username) && null == SecurityContextHolder.getContext().getAuthentication()){
            UserDetails userDetails = springSecurityService.loadUserByUsername(username);
            if (jwtTokenUtils.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print("Invalid token error.");
                return ;
            }
        }
        filterChain.doFilter(request, response);
    }
}
