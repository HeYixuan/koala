package org.igetwell.system.security.provider;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public interface AbstractClientDetails extends ClientDetailsService {

    ClientDetails loadClientByTenantClientId(String tenant, String clientId) throws ClientRegistrationException;
}
