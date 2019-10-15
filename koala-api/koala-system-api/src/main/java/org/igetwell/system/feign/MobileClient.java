package org.igetwell.system.feign;

import org.igetwell.common.uitls.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "koala-system")
public interface MobileClient {

    @PostMapping("/mobile/{mobile}")
    ResponseEntity sendSmsCode(@PathVariable("mobile") String mobile);
}
