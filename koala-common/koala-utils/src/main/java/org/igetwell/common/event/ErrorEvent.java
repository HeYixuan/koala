package org.igetwell.common.event;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ErrorEvent implements Serializable {

    /**
     * 应用名
     */
    @Nullable
    private String appName;
    /**
     * 环境
     */
    @Nullable
    private String env;
    /**
     * 远程ip 主机名
     */
    @Nullable
    private String remoteHost;
    /**
     * 请求方法名
     */
    @Nullable
    private String requestMethod;
    /**
     * 请求url
     */
    @Nullable
    private String requestUrl;
    /**
     * 堆栈信息
     */
    @Nullable
    private String stackTrace;
    /**
     * 异常名
     */
    @Nullable
    private String exceptionName;
    /**
     * 异常消息
     */
    @Nullable
    private String message;
    /**
     * 类名
     */
    @Nullable
    private String className;
    /**
     * 文件名
     */
    @Nullable
    private String fileName;
    /**
     * 方法名
     */
    @Nullable
    private String methodName;
    /**
     * 代码行数
     */
    @Nullable
    private Integer lineNumber;
    /**
     * 异常时间
     */
    @Nullable
    private LocalDateTime createTime;
}
