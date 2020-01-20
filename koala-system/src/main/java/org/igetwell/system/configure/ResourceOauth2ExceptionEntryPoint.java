package org.igetwell.system.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.CommonConstants;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 客户端异常处理
 * 1. 可以根据 AuthenticationException 不同细化异常处理
 */
@Slf4j
@Component
@AllArgsConstructor
public class ResourceOauth2ExceptionEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException exception) {
		response.setCharacterEncoding(CommonConstants.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setStatus(HttpStatus.UNAUTHORIZED.value());
		responseEntity.setMessage(HttpStatus.UNAUTHORIZED.getMessage());
		if (exception != null) {
			responseEntity.setException(exception.getMessage());
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(responseEntity));
	}
}
