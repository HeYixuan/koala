package org.igetwell.system.entity;

import javax.persistence.*;

@Table(name = "sys_role")
public class SystemRole {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 租户ID
     */
    @Column(name = "tenant_id")
    private String tenantId;

    /**
     * 父主键
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色别名
     */
    @Column(name = "role_alias")
    private String roleAlias;

    /**
     * 数据权限类型
     */
    private Integer dsType;
    /**
     * 数据权限作用范围
     */
    private String dsScope;

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
     * 获取租户ID
     *
     * @return tenant_id - 租户ID
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    /**
     * 获取父主键
     *
     * @return parent_id - 父主键
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父主键
     *
     * @param parentId 父主键
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取角色名
     *
     * @return role_name - 角色名
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名
     *
     * @param roleName 角色名
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取角色别名
     *
     * @return role_alias - 角色别名
     */
    public String getRoleAlias() {
        return roleAlias;
    }

    /**
     * 设置角色别名
     *
     * @param roleAlias 角色别名
     */
    public void setRoleAlias(String roleAlias) {
        this.roleAlias = roleAlias == null ? null : roleAlias.trim();
    }


    public Integer getDsType() {
        return dsType;
    }

    public void setDsType(Integer dsType) {
        this.dsType = dsType;
    }

    public String getDsScope() {
        return dsScope;
    }

    public void setDsScope(String dsScope) {
        this.dsScope = dsScope == null ? null : dsScope.trim();
    }
}