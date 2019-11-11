package org.igetwell.system.service.impl;

import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.mapper.SystemRoleMapper;
import org.igetwell.system.service.ISystemRoleService;
import org.igetwell.system.vo.RoleTree;
import org.igetwell.system.vo.SystemRoleVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemRoleService implements ISystemRoleService {

    @Resource
    private Sequence sequence;
    @Resource
    private SystemRoleMapper systemRoleMapper;

    @Override
    public List<SystemRole> getList() {
        return systemRoleMapper.getList();
    }

    /**
     * 获取所有角色(树节点)
     * @return
     */
    public List<RoleTree> getRoles(String tenantId){
        return systemRoleMapper.getRoles(tenantId);
    }

    @Override
    public List<SystemRoleVo> getRoleNames(List<String> idList) {
        return systemRoleMapper.getRoleNames(idList);
    }

    @Override
    public List<SystemRoleVo> loadByUser(Long id) {
        return systemRoleMapper.loadByUser(id);
    }

    @Override
    public List<SystemRoleVo> loadByTenant(String tenant, Long id) {
        return systemRoleMapper.loadByTenant(tenant, id);
    }

    @Override
    public Integer getDsType(Long id) {
        return systemRoleMapper.getDsType(id);
    }

    @Override
    public Integer getDataScopeType(String tenant, Long id) {
        return systemRoleMapper.getDataScopeType(tenant, id);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = systemRoleMapper.deleteById(id);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(SystemRole systemRole) {
        systemRole.setId(sequence.nextValue());
        int i = systemRoleMapper.insert(systemRole);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(SystemRole systemRole) {
        int i = systemRoleMapper.update(systemRole);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }
}
