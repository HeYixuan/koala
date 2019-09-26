package org.igetwell.system.bean;

import java.util.Date;

public class SystemUserBean {

    private Long id;

    /**
     * 所属租户
     */
    private String tenantName;

    /**
     * 账号
     */
    private String username;

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
     * 性别
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
