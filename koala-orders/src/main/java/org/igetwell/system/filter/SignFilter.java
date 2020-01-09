package org.igetwell.system.filter;


import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.uitls.*;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
//@Component
public class SignFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        try {
            Map<String, String> paramMap = ParamMap.getParams(requestWrapper);
            long timestamp = null == paramMap.get("timestamp") ? 0 : Long.valueOf(paramMap.get("timestamp"));
            long expire = 10 * 60 * 1000;
            if (DateUtils.getTimestamp() - timestamp > expire) {
                WebUtils.renderJson(response, ResponseEntity.error(HttpStatus.BAD_REQUEST, "验证签名错误"));
                return;
            }
            if (!SignUtils.checkSign(paramMap, "paterKey", SignType.MD5)) {
                WebUtils.renderJson(response, ResponseEntity.error(HttpStatus.BAD_REQUEST, "验证签名错误"));
                return;
            }
        } catch (Exception e) {
            log.error("前置验签错误!", e);
            WebUtils.renderJson(response, ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR));
            return;
        }
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {

    }

}
