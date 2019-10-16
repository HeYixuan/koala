package org.igetwell.oauth.security.provider;

import lombok.AllArgsConstructor;
import org.igetwell.common.constans.SecurityConstants;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;


@AllArgsConstructor
public class DataSourceClientDetailsService implements AbstractClientDetails{

    private final JdbcTemplate jdbcTemplate;

    /**
     * 根据客户端ID查询
     * @param clientId
     * @return
     * @throws ClientRegistrationException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            return jdbcTemplate.queryForObject(SecurityConstants.DEFAULT_SELECT_STATEMENT, new String[]{clientId}, new BeanPropertyRowMapper<>(ClientDetails.class));
        } catch (ClientRegistrationException ex) {
            return null;
        }
    }

    /**
     * 根据租户和客户端ID查询
     * @param tenant
     * @param clientId
     * @return
     * @throws ClientRegistrationException
     */
    @Override
    public ClientDetails loadClientByTenantClientId(String tenant, String clientId) throws ClientRegistrationException {
        try {
            return jdbcTemplate.queryForObject(SecurityConstants.DEFAULT_TENANT_SELECT_STATEMENT, new String[]{tenant, clientId}, new BeanPropertyRowMapper<>(ClientDetails.class));
        } catch (ClientRegistrationException ex) {
            return null;
        }
    }
}
