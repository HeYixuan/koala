package org.igetwell.common.sequence;

import org.igetwell.common.sequence.builder.SnowflakeSeqBuilder;
import org.igetwell.common.sequence.properties.SequenceSnowflakeProperties;
import org.igetwell.common.sequence.sequence.Sequence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.igetwell.common.sequence")
@ConditionalOnMissingBean(Sequence.class)
public class SequenceAutoConfiguration {

	/**
	 * snowflake 算法作为发号器实现
	 *
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnProperty("sequence.snowflake")
	public Sequence snowflakeSequence(SequenceSnowflakeProperties properties) {
		return SnowflakeSeqBuilder
				.create()
				.datacenterId(properties.getDatacenterId())
				.workerId(properties.getWorkerId())
				.build();
	}
}