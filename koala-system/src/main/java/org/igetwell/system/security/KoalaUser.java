package org.igetwell.system.security;

import lombok.Data;
import org.igetwell.system.entity.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class KoalaUser extends SystemUser implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public KoalaUser(Long id, String tenantId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(id, tenantId, username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked);
        this.authorities = authorities;

    }
}
