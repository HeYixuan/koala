package org.igetwell.system.feign;

import org.igetwell.system.entity.SystemMenu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "koala-system")
public interface SystemMenuClient {

    /**
     * 根据角色ID查询所有菜单
     * @param id
     * @return
     */
    @PostMapping("/systemMenu/loadByRole/{id}")
    List<SystemMenu> loadByRole(@PathVariable("id") Long id);

    /**
     * 查询菜单ID不在中间表的菜单数据
     * @return
     */
    @PostMapping("/systemMenu/loadUnbound}")
    List<SystemMenu> loadUnbound();
}
