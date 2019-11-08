package org.igetwell.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleTree extends TreeNode {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 角色别名
     */
    private String roleAlias;
}
