package org.igetwell.system.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemDept;

import java.util.List;

public interface ISystemDeptService {

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

    ResponseEntity deleteById(Long id);

    ResponseEntity insert(SystemDept systemDept);

    ResponseEntity update(SystemDept systemDept);
}
