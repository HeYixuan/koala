package org.igetwell.system.entity;

import javax.persistence.*;

@Table(name = "sys_dept")
public class SystemDept {
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
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门类型
     */
    @Column(name = "dept_category")
    private Integer deptCategory;

    /**
     * 部门名
     */
    @Column(name = "name")
    private String name;

    /**
     * 部门全称
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 排序
     */
    private Integer sort;

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
     * 获取祖级列表
     *
     * @return ancestors - 祖级列表
     */
    public String getAncestors() {
        return ancestors;
    }

    /**
     * 设置祖级列表
     *
     * @param ancestors 祖级列表
     */
    public void setAncestors(String ancestors) {
        this.ancestors = ancestors == null ? null : ancestors.trim();
    }

    /**
     * 获取部门类型
     *
     * @return dept_category - 部门类型
     */
    public Integer getDeptCategory() {
        return deptCategory;
    }

    /**
     * 设置部门类型
     *
     * @param deptCategory 部门类型
     */
    public void setDeptCategory(Integer deptCategory) {
        this.deptCategory = deptCategory;
    }

    /**
     * 获取部门名
     *
     * @return name - 部门名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置部门名
     *
     * @param name 部门名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取部门全称
     *
     * @return full_name - 部门全称
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 设置部门全称
     *
     * @param fullName 部门全称
     */
    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
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

}