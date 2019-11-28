package org.igetwell.common.sequence;

import org.igetwell.common.sequence.builder.SnowflakeSeqBuilder;
import org.igetwell.common.sequence.properties.SequenceSnowflakeProperties;
import org.igetwell.common.sequence.sequence.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设置发号器生成规则
 */
@Configuration
//@ComponentScan("org.igetwell.common.sequence")
//@ConditionalOnMissingBean(Sequence.class)
public class SequenceAutoConfiguration {

	/**
	 * snowflake 算法作为发号器实现
	 *
	 * @param properties
	 * @return
	 */
	@Bean
	//@ConditionalOnProperty("sequence.snowflake")
	public Sequence snowflakeSequence(SequenceSnowflakeProperties properties) {
		return SnowflakeSeqBuilder
				.create()
				.dataCenterId(properties.getDatacenterId())
				.workerId(properties.getWorkerId())
				.build();
	}
}