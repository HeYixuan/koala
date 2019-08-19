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
        System.err.println("用户授权信息：["+authentication.getDetails()+"],真实IP是：[" + WebUtils.getIP() + "]");
        //SystemUserDTO dto = iSystemUserService.get(authentication.getName());
        //JwtUser jwtUser = JwtUser.build(authentication.getName(), dto.getOfficeName(), dto.getDeptName(), dto.getPositionName(), authentication.getAuthorities());
        //response.setStatus(HttpStatus.OK.value());
        //response.getWriter().write(JsonMapper.INSTANCE.toJson(jwtUser));
    }
}
