package org.igetwell.system.security.handler;

import org.igetwell.common.uitls.WebUtils;
import org.igetwell.system.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

   /* @Autowired
    private JwtTokenUtil jwtTokenUtil;*/
    @Autowired
    private ISystemUserService iSystemUserService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        //String jwtToken = jwtTokenUtil.createToken(authentication.getName());
        //response.setHeader("X-API-TOKEN", "Bearer " + jwtToken);
        System.err.println("userDetail["+authentication.getDetails()+"],真实IP是：[" + WebUtils.getIP() + "]");
        //SystemUserDTO dto = iSystemUserService.get(authentication.getName());
        //JwtUser jwtUser = JwtUser.build(authentication.getName(), dto.getOfficeName(), dto.getDeptName(), dto.getPositionName(), authentication.getAuthorities());
        //response.setStatus(HttpStatus.OK.value());
        //response.getWriter().write(JsonMapper.INSTANCE.toJson(jwtUser));
    }

    /*public static final String getIPAddress(final HttpServletRequest request) throws ServletException {
        if (request == null) {
            throw (new ServletException("getIPAddress method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)){
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)){
            ipString = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)){
                ipString = str;
                break;
            }
        }
        return ipString;
    }*/
}
