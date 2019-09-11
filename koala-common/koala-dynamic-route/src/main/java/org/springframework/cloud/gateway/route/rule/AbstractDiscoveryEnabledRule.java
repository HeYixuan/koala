package org.springframework.cloud.gateway.route.rule;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.*;
import org.springframework.cloud.gateway.route.predicate.AbstractDiscoveryEnabledPredicate;
import org.springframework.util.Assert;

import java.util.List;

/**
 * ribbon 路由规则
 */
public abstract class AbstractDiscoveryEnabledRule extends PredicateBasedRule {
	private final CompositePredicate predicate;

	public AbstractDiscoveryEnabledRule(AbstractDiscoveryEnabledPredicate discoveryEnabledPredicate) {
		Assert.notNull(discoveryEnabledPredicate, "Parameter 'discoveryEnabledPredicate' can't be null");
		this.predicate = createCompositePredicate(discoveryEnabledPredicate, new AvailabilityPredicate(this, null));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractServerPredicate getPredicate() {
		return predicate;
	}

	@Override
	public Server choose(Object key) {
		ILoadBalancer lb = getLoadBalancer();

		List<Server> allServers = lb.getAllServers();
		// 过滤服务列表
		List<Server> serverList = filterServers(allServers);

		Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(serverList, key);
		if (server.isPresent()) {
			return server.get();
		} else {
			return null;
		}
	}

	/**
	 * 过滤服务
	 *
	 * @param serverList 服务列表
	 * @return 服务列表
	 */
	public abstract List<Server> filterServers(List<Server> serverList);

	private CompositePredicate createCompositePredicate(AbstractDiscoveryEnabledPredicate discoveryEnabledPredicate, AvailabilityPredicate availabilityPredicate) {
		return CompositePredicate.withPredicates(discoveryEnabledPredicate, availabilityPredicate)
				.build();
	}
}
