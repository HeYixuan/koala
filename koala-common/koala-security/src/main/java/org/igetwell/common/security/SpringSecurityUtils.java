package org.igetwell.common.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 *
 */
@UtilityClass
public class SpringSecurityUtils {

    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     *
     * @param authentication
     * 获取当前用户的全部信息 EnableKoalaResourceServer true
     * 获取当前用户的用户名 EnableKoalaResourceServer false
     */
    public KoalaUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof KoalaUser) {
            return (KoalaUser) principal;
        }
        return null;
    }

    /**
     * 获取用户
     */
    public KoalaUser getUser() {
        Authentication authentication = getAuthentication();
        return getUser(authentication);
    }
}
