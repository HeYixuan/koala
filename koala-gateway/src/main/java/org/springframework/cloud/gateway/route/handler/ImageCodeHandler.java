package org.springframework.cloud.gateway.route.handler;

import com.google.code.kaptcha.Producer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.CommonConstants;
import org.igetwell.common.constans.SecurityConstants;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码生成逻辑处理类
 */
@Slf4j
@Component
@AllArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {
	private static final String JPEG = "jpeg";
	private final Producer producer;
	private final RedisTemplate redisTemplate;

	@Override
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		//生成验证码
		String text = producer.createText();
		BufferedImage image = producer.createImage(text);

		//保存验证码信息
		String randomStr = serverRequest.queryParam("randomStr").get();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.opsForValue().set(CommonConstants.DEFAULT_CODE_KEY + randomStr, text
			, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);

		// 转换流信息写出
		FastByteArrayOutputStream os = new FastByteArrayOutputStream();
		try {
			ImageIO.write(image, JPEG, os);
		} catch (IOException e) {
			log.error("ImageIO write err", e);
			return Mono.error(e);
		}

		return ServerResponse
			.status(HttpStatus.OK)
			.contentType(MediaType.IMAGE_JPEG)
			.body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
	}
}
