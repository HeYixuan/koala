package org.springframework.cloud.gateway.route.predicate;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.CommonConstants;
import org.igetwell.common.uitls.SpringContextHolder;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.cloud.gateway.route.support.KoalaRibbonRuleProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 基于 Metadata version 的服务筛选
 *
 */
@Slf4j
public class GrayMetadataAwarePredicate extends AbstractDiscoveryEnabledPredicate {

	@Override
	protected boolean apply(NacosServer server, HttpHeaders headers) {
		KoalaRibbonRuleProperties ribbonProperties = SpringContextHolder.getBean(KoalaRibbonRuleProperties.class);

		if (!ribbonProperties.isGrayEnabled()) {
			log.debug("gray closed,GrayMetadataAwarePredicate return true");
			return true;
		}

		final Map<String, String> metadata = server.getMetadata();
		String version = metadata.get(CommonConstants.VERSION);
		// 判断Nacos服务是否有版本标签
		if (StringUtils.isEmpty(version)) {
			log.debug("nacos server tag is blank ,GrayMetadataAwarePredicate return true");
			return true;
		}

		// 判断请求中是否有版本
		String target = headers.getFirst(CommonConstants.VERSION);
		if (StringUtils.isEmpty(target)) {
			log.debug("request headers version is blank,GrayMetadataAwarePredicate return true");
			return true;
		}

		log.debug("请求版本:{} ,当前服务版本:{}", target, version);
		return target.equals(version);
	}

}
