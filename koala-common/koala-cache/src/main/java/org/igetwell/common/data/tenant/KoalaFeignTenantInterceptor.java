package org.igetwell.common.data.tenant;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


@Slf4j
public class KoalaFeignTenantInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate requestTemplate) {
		/*if (StringUtils.isEmpty(TenantContextHolder.getTenantId())) {
			log.error("TTL 中的 租户ID为空，feign拦截器 >> 增强失败");
			TenantContextHolder.setTenantId("000000");
			return;
		}
		requestTemplate.header(Tenant.TENANT_ID, TenantContextHolder.getTenantId());*/

		if (!StringUtils.isEmpty(TenantContextHolder.getTenantId())) {
			log.error("TTL 中的 租户ID为空，feign拦截器 >> 增强失败");
			//TenantContextHolder.setTenantId("000000");
			requestTemplate.header(Tenant.TENANT_ID, TenantContextHolder.getTenantId());
			//return;
		}

	}
}
