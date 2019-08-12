/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.alibaba.nacos.console.config;

/**
 * @author lengleng
 * @date 2019-07-16
 */

/**
 * @author lengleng
 * @date 2019-04-19
 * <p>
 * 覆盖nacos 默认配置
 */
public interface ConfigConstants {
	/**
	 * The System property name of  Standalone mode
	 */
	String STANDALONE_MODE = "nacos.standalone";

	/**
	 * tomcat 目录
	 */
	String TOMCAT_DIR = "server.tomcat.basedir";

	/**
	 * tomcat 日志配置
	 */
	String TOMCAT_ACCESS_LOG = "server.tomcat.accesslog.enabled";

}