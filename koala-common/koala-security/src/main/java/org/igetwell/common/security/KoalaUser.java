package org.igetwell.common.security;

import org.igetwell.system.entity.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class KoalaUser extends SystemUser implements UserDetails {


    private Collection<? extends GrantedAuthority> authorities;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public KoalaUser(Long id, String tenantId, String deptId, String username, String password, boolean isEnabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(id, tenantId, deptId, username, password, isEnabled, accountNonExpired, credentialsNonExpired, accountNonLocked);
        this.authorities = authorities;

    }
}
