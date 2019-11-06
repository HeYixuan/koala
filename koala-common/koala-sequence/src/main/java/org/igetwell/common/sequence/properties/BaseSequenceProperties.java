package org.igetwell.common.sequence.properties;

import lombok.Data;

/**
 * @author lengleng
 * @date 2019-05-26
 * <p>
 * 发号器通用属性
 */
@Data
class BaseSequenceProperties {
	/**
	 * 获取range步长[可选，默认：1000]
	 */
	private int step = 1000;

	/**
	 * 序列号分配起始值[可选：默认：0]
	 */
	private long stepStart = 0;

	/**
	 * 业务名称
	 */
	private String bizName = "pigx";
}
