package org.igetwell.common.sequence.builder;

import org.igetwell.common.sequence.sequence.Sequence;

/**
 * 序列号生成器构建者
 */
public interface SeqBuilder {

	/**
	 * 构建一个序列号生成器
	 *
	 * @return 序列号生成器
	 */
	Sequence build();

}
