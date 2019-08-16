package org.igetwell.system.entity;

import javax.persistence.*;

@Table(name = "sys_menu")
public class SystemMenu {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父级菜单
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 菜单编号
     */
    private String code;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单别名
     */
    private String alias;

    /**
     * 请求地址
     */
    private String path;

    /**
     * 菜单资源
     */
    private String source;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单类型
     */
    private Integer category;

    /**
     * 操作按钮类型
     */
    private Integer action;

    /**
     * 0开启 1关闭
     */
    @Column(name = "keep_alive")
    private Integer keepAlive;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

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
     * 获取父级菜单
     *
     * @return parent_id - 父级菜单
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父级菜单
     *
     * @param parentId 父级菜单
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取菜单编号
     *
     * @return code - 菜单编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置菜单编号
     *
     * @param code 菜单编号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取菜单名称
     *
     * @return name - 菜单名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置菜单名称
     *
     * @param name 菜单名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取菜单别名
     *
     * @return alias - 菜单别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置菜单别名
     *
     * @param alias 菜单别名
     */
    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    /**
     * 获取请求地址
     *
     * @return path - 请求地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置请求地址
     *
     * @param path 请求地址
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * 获取菜单资源
     *
     * @return source - 菜单资源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置菜单资源
     *
     * @param source 菜单资源
     */
    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取菜单类型
     *
     * @return category - 菜单类型
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置菜单类型
     *
     * @param category 菜单类型
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取操作按钮类型
     *
     * @return action - 操作按钮类型
     */
    public Integer getAction() {
        return action;
    }

    /**
     * 设置操作按钮类型
     *
     * @param action 操作按钮类型
     */
    public void setAction(Integer action) {
        this.action = action;
    }

    /**
     * 获取0开启 1关闭
     *
     * @return keep_alive - 0开启 1关闭
     */
    public Integer getKeepAlive() {
        return keepAlive;
    }

    /**
     * 设置0开启 1关闭
     *
     * @param keepAlive 0开启 1关闭
     */
    public void setKeepAlive(Integer keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取是否已删除
     *
     * @return is_deleted - 是否已删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否已删除
     *
     * @param isDeleted 是否已删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}