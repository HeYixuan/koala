package org.igetwell.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置信息
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
	/**
	 * Minio 服务地址 http://ip:port
	 */
	private String url;

	/**
	 * 用户名
	 */
	private String accessKey;

	/**
	 * 密码
	 */
	private String secretKey;

}
