package org.igetwell.system.security.provider;

/**
 * 多终端注册接口
 *
 * @author Chill
 */
public interface IClientDetailsService {

	/**
	 * 根据clientId获取Client详情
	 *
	 * @param clientId 客户端id
	 * @return
	 */
	IClientDetails loadClientByClientId(String clientId);

	/**
	 * 根据租户和客户端ID查询
	 * @param tenant
	 * @param clientId
	 * @return
	 */
	IClientDetails loadClientByTenantClientId(String tenant, String clientId);

}
