package org.igetwell.system.security.mobile.authentication;

import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机号验证码登陆
 */
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    public static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "captcha";
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String captchaParameter  = SPRING_SECURITY_FORM_CAPTCHA_KEY;
    private boolean postOnly = true;
    @Resource
    private AuthenticationEventPublisher eventPublisher;
    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    public MobileAuthenticationFilter() {
        super(new AntPathRequestMatcher("/mobile/login", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String mobile = this.obtainMobile(request);
            String captcha = this.obtainCaptcha(request);
            if (mobile == null) {
                mobile = "";
            }

            if (captcha == null) {
                captcha = "";
            }

            mobile = mobile.trim();
            captcha = captcha.trim();
            MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile, captcha);
            this.setDetails(request, mobileAuthenticationToken);
            Authentication authResult = null;
            try {
                authResult = this.getAuthenticationManager().authenticate(mobileAuthenticationToken);
                logger.debug("Authentication success: " + authResult);
                SecurityContextHolder.getContext().setAuthentication(authResult);

            } catch (Exception failed) {
                SecurityContextHolder.clearContext();
                logger.debug("Authentication request failed: " + failed);

                eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed),
                        new PreAuthenticatedAuthenticationToken("access-token", "N/A"));

                try {
                    authenticationEntryPoint.commence(request, response,
                            new UsernameNotFoundException(failed.getMessage(), failed));
                } catch (Exception e) {
                    logger.error("authenticationEntryPoint handle error:{}", failed);
                }
            }

            return authResult;
        }
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }

    protected String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(this.captchaParameter);
    }


    protected void setDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "Username parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public void setCaptchaParameter(String captchaParameter) {
        Assert.hasText(captchaParameter, "Password parameter must not be empty or null");
        this.captchaParameter = captchaParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return this.mobileParameter;
    }

    public final String getCaptchaParameter() {
        return this.captchaParameter;
    }
}
