package org.igetwell.system.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemUserPageDto;
import org.igetwell.system.entity.SystemUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
     * 登录(根据手机号查询)
     * @param mobile
     * @return
     */
    @PostMapping("/systemUser/loadByMobile/{mobile}/")
    SystemUser loadByMobile(@PathVariable("mobile") String mobile);

    /**
     * 获取用户列表
     * @return
     */
    @PostMapping("/systemUser/getList")
    ResponseEntity getList(@RequestBody SystemUserPageDto dto);

    /**
     * 新增用户
     * @param systemUser
     * @return
     */
    @PostMapping("/systemUser/add")
    ResponseEntity insert(@RequestBody SystemUser systemUser);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    @PostMapping("/systemUser/deleteById/{id}")
    ResponseEntity deleteById(@PathVariable("id") Long id);

    /**
     * 修改用户
     * @param systemUser
     * @return
     */
    @PostMapping("/systemUser/update")
    ResponseEntity update(@RequestBody SystemUser systemUser);

    @PostMapping("/systemUser/test")
    ResponseEntity test();
}
