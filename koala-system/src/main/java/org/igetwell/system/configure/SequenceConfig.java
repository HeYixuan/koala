package org.igetwell.system.configure;

import org.igetwell.common.sequence.builder.SnowflakeSeqBuilder;
import org.igetwell.common.sequence.properties.SequenceSnowflakeProperties;
import org.igetwell.common.sequence.sequence.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 设置发号器生成规则
 */
@Configuration
public class SequenceConfig {

	@Bean
	public Sequence snowflakeSequence(SequenceSnowflakeProperties properties) {
		return SnowflakeSeqBuilder
				.create()
				.dataCenterId(properties.getDatacenterId())
				.workerId(properties.getWorkerId())
				.build();
	}
}
