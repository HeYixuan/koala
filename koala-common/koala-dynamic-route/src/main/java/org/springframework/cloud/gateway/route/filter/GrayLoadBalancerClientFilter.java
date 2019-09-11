package org.springframework.cloud.gateway.route.filter;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;


public class GrayLoadBalancerClientFilter extends LoadBalancerClientFilter {
	private final RibbonLoadBalancerClient loadBalancer;

	public GrayLoadBalancerClientFilter(RibbonLoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
		super(loadBalancer, properties);
		this.loadBalancer = loadBalancer;
	}

	@Override
	protected ServiceInstance choose(ServerWebExchange exchange) {
		HttpHeaders headers = exchange.getRequest().getHeaders();
		return loadBalancer.choose(((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost(), headers);
	}

}
