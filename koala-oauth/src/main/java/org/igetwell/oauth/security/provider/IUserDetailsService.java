package org.igetwell.oauth.security.provider;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserDetailsService extends UserDetailsService {

    /**
     * 根据手机号登录
     * @param mobile
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;
}
