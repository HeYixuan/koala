package org.igetwell.system.dto;

import lombok.Data;

@Data
public class SystemUserPageDto {

    private String username;

    private String email;

    private String mobile;

    private int pageNo = 1;
    private int pageSize = 10;
}
