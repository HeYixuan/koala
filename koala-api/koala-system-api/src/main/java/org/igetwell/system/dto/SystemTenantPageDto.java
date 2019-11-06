package org.igetwell.system.dto;

import lombok.Data;

@Data
public class SystemTenantPageDto {

    private String tenantId;

    private String tenantName;


    private int pageNo = 1;
    private int pageSize = 10;

}
