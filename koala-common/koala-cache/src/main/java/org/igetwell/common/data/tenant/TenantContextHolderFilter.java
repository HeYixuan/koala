package org.igetwell.common.data.tenant;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.TenantConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String tenantId = request.getHeader(TenantConstants.TENANT_ID);
		log.debug("获取header中的租户ID为:{}", tenantId);

		if (!StringUtils.isEmpty(tenantId)) {
			TenantContextHolder.setTenantId(tenantId);
		} else {
			TenantContextHolder.setTenantId(TenantConstants.SUPER_TENANT_ID);
		}

		filterChain.doFilter(request, response);
		TenantContextHolder.clear();
	}
}
