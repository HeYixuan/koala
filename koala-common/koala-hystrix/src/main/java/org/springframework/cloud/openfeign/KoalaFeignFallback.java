package org.springframework.cloud.openfeign;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.common.uitls.SpringContextHolder;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 * fallback 代理处理
 */
@Slf4j
@AllArgsConstructor
public class KoalaFeignFallback<T> implements MethodInterceptor {
	private final Class<T> targetType;
	private final String targetName;
	private final Throwable cause;

	@Nullable
	@Override
	@SneakyThrows
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
		log.error("KoalaFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, cause.getMessage());
		Class<?> returnType = method.getReturnType();
		// 暂时不支持 flux，rx，异步等，返回值不是 ResponseEntity，直接返回 null。
		if (ResponseEntity.class != returnType) {
			return null;
		}
		// 非 FeignException，直接返回请求被拒绝
		if (!(cause instanceof FeignException) || (cause instanceof RetryableException)) {
			return new ResponseEntity(HttpStatus.FORBIDDEN, cause.getMessage());
		}

		FeignException exception = (FeignException) cause;

		byte[] content = exception.getMessage().getBytes();

		String str = StrUtil.str(content, StandardCharsets.UTF_8);

		log.error("KoalaFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, str);
		ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);
		return objectMapper.readValue(str, ResponseEntity.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		KoalaFeignFallback<?> that = (KoalaFeignFallback<?>) o;
		return targetType.equals(that.targetType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetType);
	}
}
