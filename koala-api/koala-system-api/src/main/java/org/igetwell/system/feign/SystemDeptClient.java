package org.igetwell.system.feign;

import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(value = "koala-system")
public interface SystemDeptClient {

    /**
     * 获取部门子级
     * 单表(只查询部门表)
     * @param deptId 部门id
     * @return deptIds
     */
    List<Long> getDeptAncestors(Long deptId);

    /**
     * 获取部门子级
     * 中间表(查询部门关系表)
     * @param deptId 部门id
     * @return deptIds
     */
    List<Long> getAncestors(Long deptId);
}
