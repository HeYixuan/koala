package org.igetwell.system.feign;

import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(contextId = "remoteDeptService", value = "system-center")
public interface SystemDeptClient {

    /**
     * 获取部门子级
     * 单表(只查询部门表)
     * @param deptId 部门id
     * @return deptIds
     */
    public List<Long> getDeptAncestors(Long deptId);

    /**
     * 获取部门子级
     * 中间表(查询部门关系表)
     * @param deptId 部门id
     * @return deptIds
     */
    public List<Long> getAncestors(Long deptId);
}
