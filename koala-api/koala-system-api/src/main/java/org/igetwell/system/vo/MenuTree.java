package org.igetwell.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode {
	/**
	 * 菜单图标
	 */
	private String iconClass;

	/**
	 * 菜单编码
	 */
	private String code;

	/**
	 *  菜单别名
	 */
	private String alias;

	/**
	 * 请求地址
	 */
	private String uri;

	/**
	 * 路由缓冲
	 */
	private String keepAlive;


	/**
	 * 菜单类型: 0-菜单 1-按钮
	 */
	private String menuType;


	/**
	 * 排序值
	 */
	private int sort;


}
