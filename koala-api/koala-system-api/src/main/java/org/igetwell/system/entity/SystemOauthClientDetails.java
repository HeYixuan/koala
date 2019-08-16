package org.igetwell.system.entity;

import javax.persistence.*;

@Table(name = "sys_oauth_client_details")
public class SystemOauthClientDetails {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户端id
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @Column(name = "client_secret")
    private String clientSecret;

    /**
     * 资源集合
     */
    @Column(name = "resource_ids")
    private String resourceIds;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 授权类型
     */
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    /**
     * 权限
     */
    private String authorities;

    /**
     * 令牌过期秒数
     */
    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    /**
     * 刷新令牌过期秒数
     */
    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    /**
     * 附加说明
     */
    @Column(name = "additional_information")
    private String additionalInformation;

    /**
     * 自动授权
     */
    private String autoapprove;

    /**
     * 所属租户
     */
    @Column(name = "tenant_id")
    private Integer tenantId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取客户端id
     *
     * @return client_id - 客户端id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置客户端id
     *
     * @param clientId 客户端id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    /**
     * 获取客户端密钥
     *
     * @return client_secret - 客户端密钥
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * 设置客户端密钥
     *
     * @param clientSecret 客户端密钥
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret == null ? null : clientSecret.trim();
    }

    /**
     * 获取资源集合
     *
     * @return resource_ids - 资源集合
     */
    public String getResourceIds() {
        return resourceIds;
    }

    /**
     * 设置资源集合
     *
     * @param resourceIds 资源集合
     */
    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds == null ? null : resourceIds.trim();
    }

    /**
     * 获取授权范围
     *
     * @return scope - 授权范围
     */
    public String getScope() {
        return scope;
    }

    /**
     * 设置授权范围
     *
     * @param scope 授权范围
     */
    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    /**
     * 获取授权类型
     *
     * @return authorized_grant_types - 授权类型
     */
    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    /**
     * 设置授权类型
     *
     * @param authorizedGrantTypes 授权类型
     */
    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes == null ? null : authorizedGrantTypes.trim();
    }

    /**
     * 获取回调地址
     *
     * @return web_server_redirect_uri - 回调地址
     */
    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    /**
     * 设置回调地址
     *
     * @param webServerRedirectUri 回调地址
     */
    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri == null ? null : webServerRedirectUri.trim();
    }

    /**
     * 获取权限
     *
     * @return authorities - 权限
     */
    public String getAuthorities() {
        return authorities;
    }

    /**
     * 设置权限
     *
     * @param authorities 权限
     */
    public void setAuthorities(String authorities) {
        this.authorities = authorities == null ? null : authorities.trim();
    }

    /**
     * 获取令牌过期秒数
     *
     * @return access_token_validity - 令牌过期秒数
     */
    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    /**
     * 设置令牌过期秒数
     *
     * @param accessTokenValidity 令牌过期秒数
     */
    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    /**
     * 获取刷新令牌过期秒数
     *
     * @return refresh_token_validity - 刷新令牌过期秒数
     */
    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    /**
     * 设置刷新令牌过期秒数
     *
     * @param refreshTokenValidity 刷新令牌过期秒数
     */
    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    /**
     * 获取附加说明
     *
     * @return additional_information - 附加说明
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * 设置附加说明
     *
     * @param additionalInformation 附加说明
     */
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation == null ? null : additionalInformation.trim();
    }

    /**
     * 获取自动授权
     *
     * @return autoapprove - 自动授权
     */
    public String getAutoapprove() {
        return autoapprove;
    }

    /**
     * 设置自动授权
     *
     * @param autoapprove 自动授权
     */
    public void setAutoapprove(String autoapprove) {
        this.autoapprove = autoapprove == null ? null : autoapprove.trim();
    }

    /**
     * 获取所属租户
     *
     * @return tenant_id - 所属租户
     */
    public Integer getTenantId() {
        return tenantId;
    }

    /**
     * 设置所属租户
     *
     * @param tenantId 所属租户
     */
    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}