package org.igetwell.oauth.security.extractor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenExtractor implements TokenExtractor {
	@Override
	public Authentication extract(HttpServletRequest request) {
		String tokenValue = this.extractToken(request);
		if (tokenValue != null) {
			PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
			return authentication;
		} else {
			return null;
		}
	}



	protected String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")){
			return null;
		}
		return header.substring(7);
	}
}
