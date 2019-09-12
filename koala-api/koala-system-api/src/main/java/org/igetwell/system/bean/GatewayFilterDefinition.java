package org.igetwell.system.bean;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路由过滤器模型
 */
@EqualsAndHashCode
@Builder
@Data
public class GatewayFilterDefinition {
    private String name;
    private Map<String, String> args = new LinkedHashMap<>();
}
