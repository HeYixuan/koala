package org.igetwell.system.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "koala-system")
public interface SystemUserClient {

    /**
     * 登录(根据租户ID和用户名查询)
     * @param tenant 租户ID
     * @param username 用户名
     * @return
     */
    @PostMapping("/systemUser/loadByUsername/{tenant}/{username}")
    SystemUser loadByUsername(@PathVariable("tenant") String tenant, @PathVariable("username") String username);

    /**
     * 获取用户列表
     * @return
     */
    @PostMapping("/systemUser/getList")
    List<SystemUser> getList();

    @PostMapping("/systemUser/test")
    ResponseEntity test();
}
