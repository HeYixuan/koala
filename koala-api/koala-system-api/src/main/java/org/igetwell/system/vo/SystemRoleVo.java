package org.igetwell.system.vo;

import lombok.Data;

@Data
public class SystemRoleVo {

    /**
     * 角色ID
     */
    private String id;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色别名
     */
    private String roleAlias;
}
