package org.igetwell.system.feign;

import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.vo.SystemRoleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "koala-system")
public interface SystemRoleClient {

    /**
     * 获取所有角色
     * @return
     */
    @PostMapping("/systemRole/getList")
    List<SystemRole> getList();

    /**
     * 获取角色名
     * @param idList
     * @return
     */
    @PostMapping("/systemRole/getRoleNames")
    List<SystemRoleVo> getRoleNames(@RequestBody List<String> idList);


    /**
     * 根据用户ID查询所有角色
     * @param id
     * @return
     */
    @PostMapping("/systemRole/loadByUser/{id}")
    List<SystemRoleVo> loadByUser(@PathVariable("id") Long id);

    /**
     * 根据租户ID和用户ID查询所有角色
     * @param tenant
     * @param id
     * @return
     */
    @PostMapping("/systemRole/loadByTenant/{tenant}/{id}")
    List<SystemRoleVo> loadByTenant(@PathVariable("tenant") String tenant, @PathVariable("id") Long id);
}
