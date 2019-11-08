package org.igetwell.system.entity;

import java.util.Date;

public class SystemGatewayRoute {
    /**
     * 路由ID
     */
    private String id;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 断言
     */
    private String predicates;

    /**
     * 过滤器
     */
    private String filters;

    /**
     * 路径
     */
    private String uri;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除标记
     */
    private String delFlag;

    /**
     * 获取路由ID
     *
     * @return id - 路由ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置路由ID
     *
     * @param id 路由ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取路由名称
     *
     * @return route_name - 路由名称
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * 设置路由名称
     *
     * @param routeName 路由名称
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName == null ? null : routeName.trim();
    }

    /**
     * 获取断言
     *
     * @return predicates - 断言
     */
    public String getPredicates() {
        return predicates;
    }

    /**
     * 设置断言
     *
     * @param predicates 断言
     */
    public void setPredicates(String predicates) {
        this.predicates = predicates;
    }

    /**
     * 获取过滤器
     *
     * @return filters - 过滤器
     */
    public String getFilters() {
        return filters;
    }

    /**
     * 设置过滤器
     *
     * @param filters 过滤器
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }

    /**
     * 获取路径
     *
     * @return uri - 路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置路径
     *
     * @param uri 路径
     */
    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    /**
     * 获取排序
     *
     * @return order - 排序
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * 设置排序
     *
     * @param order 排序
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取删除标记
     *
     * @return del_flag - 删除标记
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * 设置删除标记
     *
     * @param delFlag 删除标记
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }
}