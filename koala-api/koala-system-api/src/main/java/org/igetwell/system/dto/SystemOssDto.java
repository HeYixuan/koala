package org.igetwell.system.dto;

import lombok.Data;

@Data
public class SystemOssDto {

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 空间名
     */
    private String bucketName;
    /**
     * accessKey
     */
    private String accessKey;
    /**
     * 状态：0停用 1正常
     */
    private String status;

    private int pageNo = 1;
    private int pageSize = 10;
}
