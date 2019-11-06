package org.igetwell.system.dto;

import lombok.Data;

@Data
public class SystemOauthClientDetailsPageDto {

    private String clientId;

    private String tenantId;

    private int pageNo = 1;
    private int pageSize = 10;
}
