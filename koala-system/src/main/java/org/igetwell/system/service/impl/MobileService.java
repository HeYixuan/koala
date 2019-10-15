package org.igetwell.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.constans.CommonConstants;
import org.igetwell.common.constans.SecurityConstants;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.enums.LoginTypeEnum;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.entity.SystemUser;
import org.igetwell.system.mapper.SystemUserMapper;
import org.igetwell.system.service.IMobileService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MobileService implements IMobileService {

    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 发送手机验证码
     * TODO: 调用短信网关发送验证码,测试返回前端
     * @param mobile
     * @return
     */
    @Override
    public ResponseEntity sendSmsCode(String mobile) {
        if (StringUtils.isEmpty(mobile) || !StringUtils.hasText(mobile)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST,"手机号不合法");
        }
        SystemUser systemUser = systemUserMapper.loadByMobile(mobile);
        if (systemUser == null){
            log.info("手机号未注册:{}", mobile);
            return ResponseEntity.error(HttpStatus.BAD_REQUEST,"手机号未注册");
        }

        Object code = redisTemplate.opsForValue().get(CommonConstants.DEFAULT_CODE_KEY + LoginTypeEnum.MOBILE.getType() + "#" + mobile);

        if (!StringUtils.isEmpty(code)) {
            log.info("手机号验证码未过期:{}，{}", mobile, code);
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "验证码发送过于频繁");
        }

        String randomCode = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
        log.debug("手机号生成验证码成功:{},{}", mobile, randomCode);
        redisTemplate.opsForValue().set(CommonConstants.DEFAULT_CODE_KEY + LoginTypeEnum.MOBILE.getType() + "#" + mobile, randomCode, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
        return ResponseEntity.ok();
    }
}
