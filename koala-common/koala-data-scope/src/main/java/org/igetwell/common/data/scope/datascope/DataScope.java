package org.igetwell.common.data.scope.datascope;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * 数据权限查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataScope extends HashMap {
	/**
	 * 限制范围的字段名称
	 */
	private String scopeName = "deptId";

	/**
	 * 具体的数据范围
	 */
	private List<Integer> deptIds = new ArrayList<>();

	/**
	 * 是否只查询本部门
	 */
	private Boolean isOnly = false;
}
