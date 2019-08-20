package org.igetwell.common.data.scope.annotation;


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

}
