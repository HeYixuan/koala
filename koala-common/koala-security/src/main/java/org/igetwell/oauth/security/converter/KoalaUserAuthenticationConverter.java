package org.igetwell.oauth.security.converter;

import org.igetwell.common.constans.SecurityConstants;
import org.igetwell.oauth.security.KoalaUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class KoalaUserAuthenticationConverter implements UserAuthenticationConverter {

    private static final String N_A = "N/A";
    final String USERNAME = "username";

    /**
     * Extract information about the user to be used in an access token (i.e. for resource servers).
     *
     * @param authentication an authentication representing a user
     * @return a map of key values representing the unique information about the user
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an Authentication from a map.
     *
     * @param map a map of user information
     * @return an Authentication representing the user or null if there is none
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

            String username = (String) map.get(USERNAME);
            Long id = (Long) map.get(SecurityConstants.DETAILS_USER_ID);
            String tenantId = (String) map.get(SecurityConstants.DETAILS_TENANT_ID);
            String deptId = (String) map.get(SecurityConstants.DETAILS_DEPT_ID);
            String roleId = (String) map.get(SecurityConstants.DETAILS_ROLE_ID);
            KoalaUser systemUser = new KoalaUser(id, tenantId, roleId, deptId, username, N_A, true
                    , true, true, true, authorities);
            return new UsernamePasswordAuthenticationToken(systemUser, N_A, authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
