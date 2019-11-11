package org.igetwell.common.sequence.builder;

import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.sequence.sequence.impl.SnowflakeSequence;

/**
 * 基于雪花算法，序列号生成器构建者
 */
public class SnowflakeSeqBuilder implements SeqBuilder {

	/**
	 * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
	 */
	private long dataCenterId;
	/**
	 * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
	 */
	private long workerId;

	public static SnowflakeSeqBuilder create() {
		SnowflakeSeqBuilder builder = new SnowflakeSeqBuilder();
		return builder;
	}

	@Override
	public Sequence build() {
		SnowflakeSequence sequence = new SnowflakeSequence();
		sequence.setDatacenterId(this.dataCenterId);
		sequence.setWorkerId(this.workerId);
		return sequence;
	}

	public SnowflakeSeqBuilder dataCenterId(long dataCenterId) {
		this.dataCenterId = dataCenterId;
		return this;
	}

	public SnowflakeSeqBuilder workerId(long workerId) {
		this.workerId = workerId;
		return this;
	}

}
