package org.igetwell.common.data.scope.annotation;


import org.igetwell.common.data.scope.enums.DataScopeEnum;

import java.lang.annotation.*;

/**
 * 数据权限定义
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataScopeAuth {
    /**
     * 数据权限对应字段
     */
    String column() default "dept_id";

    /**
     * 数据权限规则
     */
    DataScopeEnum type() default DataScopeEnum.ALL;

    /**
     * 数据权限规则值域
     */
    String value() default "";

    /**
     * 是否只查询本部门
     */
    boolean isOnly() default false;
    /**
     * 是否只查询本人
     */
    boolean isCurrentUser() default false;
}
