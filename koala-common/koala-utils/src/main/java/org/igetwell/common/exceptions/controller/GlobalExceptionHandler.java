package org.igetwell.common.exceptions.controller;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.exceptions.BaseException;
import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity defaultErrorHandler(HttpServletRequest request, Exception e) {
        log.error("---DefaultException Handler---Host {} invokes url {} ERROR: {}", request.getRemoteHost(), request.getRequestURL(), e.getMessage());

        if (e instanceof IllegalArgumentException || e instanceof MethodArgumentTypeMismatchException || e instanceof MethodArgumentNotValidException){
            //400 非法请求参数
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST, e.getMessage());
        } /*else if (e instanceof AccessDeniedException){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED, e.getMessage());
        } else if (e instanceof NoHandlerFoundException){
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND, e.getMessage());
        }*/ else if (e instanceof BaseException){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (log.isErrorEnabled()){
            log.error("系统异常! {}", e);
        }
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
