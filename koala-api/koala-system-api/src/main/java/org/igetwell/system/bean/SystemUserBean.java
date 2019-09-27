package org.igetwell.system.bean;

import lombok.Data;

import java.util.Date;

@Data
public class SystemUserBean {

    private Long id;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 所属租户
     */
    private String tenantName;

    /**
     * 账号
     */
    private String username;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别：M-男，F-女，N-未知
     */
    private char sex;

    /**
     * 所属角色
     */
    private String roleName;

    /**
     * 所属部门
     */
    private String deptName;

    /**
     * 账户启用状态：0停用 1启用
     */
    private boolean isEnabled;

}
