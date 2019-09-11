package org.springframework.cloud.gateway.route.predicate;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * 过滤服务
 *
 */
public abstract class AbstractDiscoveryEnabledPredicate extends AbstractServerPredicate {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean apply(@Nullable PredicateKey input) {
		return input != null
				&& input.getServer() instanceof NacosServer
				&& apply((NacosServer) input.getServer(), (HttpHeaders) input.getLoadBalancerKey());
	}

	/**
	 * Returns whether the specific {@link NacosServer} matches this predicate.
	 *
	 * @param server  the discovered server
	 * @param headers 请求头
	 * @return whether the server matches the predicate
	 */
	abstract boolean apply(NacosServer server, HttpHeaders headers);
}
