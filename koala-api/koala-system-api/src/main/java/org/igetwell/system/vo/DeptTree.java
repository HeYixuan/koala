package org.igetwell.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 部门类型
     */
    private Integer deptCategory;

    /**
     * 部门全称
     */
    private String fullName;

    /**
     * 排序
     */
    private int sort;
}
