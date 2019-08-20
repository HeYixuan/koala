package org.igetwell.common.data.scope.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限类型
 */
@Getter
@AllArgsConstructor
public enum  DataScopeEnum {

    /**
     * 全部数据
     */
    ALL(1, "全部"),

    /**
     * 本人可见
     */
    OWN(2, "本人可见"),

    /**
     * 所在机构可见
     */
    OWN_DEPT(3, "所在机构本级可见"),

    /**
     * 所在机构及子级可见
     */
    OWN_DEPT_CHILD(4, "所在机构本级及子级可见"),

    /**
     * 自定义
     */
    CUSTOM(5, "自定义");

    /**
     * 类型
     */
    private final int type;
    /**
     * 描述
     */
    private final String description;

    public static DataScopeEnum of(Integer dataScopeType) {
        if (dataScopeType == null) {
            return null;
        }
        DataScopeEnum[] values = DataScopeEnum.values();
        for (DataScopeEnum scopeTypeEnum : values) {
            if (scopeTypeEnum.type == dataScopeType) {
                return scopeTypeEnum;
            }
        }
        return null;
    }
}
