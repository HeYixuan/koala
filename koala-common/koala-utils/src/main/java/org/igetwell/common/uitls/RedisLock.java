package org.igetwell.common.uitls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static long LOCK_EXPIRE = 30 * 1000L;

    /**
     * 加锁
     * @param key redis key
     * @param expire 过期时间，单位秒
     * @return true:加锁成功，false，加锁失败
     */
    public boolean lock(String key, String expire){

        boolean bool = redisTemplate.opsForValue().setIfAbsent(key, expire);
        if (bool){
            redisTemplate.expire(key, LOCK_EXPIRE , TimeUnit.MILLISECONDS);
            log.info("加锁成功, {}", key);
            return true;//加锁成功就返回true
        }
        String expireValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(expireValue) && Long.parseLong(expireValue) < System.currentTimeMillis()){
            String oldValue = redisTemplate.opsForValue().getAndSet(key, expire);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(expireValue)){//上一个时间不为空,并且等于当前时间
                log.info("加锁成功, {}", key);
                return true;
            }
        }
        return  false;//失败返回false
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key,String value){//执行删除可能出现异常需要捕获
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {//如果不为空,就删除锁
                redisTemplate.delete(key);
            }
        }catch (Exception e){
            log.error("[redis分布式锁] 解锁失败!", e);
        }
    }

    /**
     * 解锁
     * @param key
     */
    public void unlock(String key){
        redisTemplate.delete(key);
    }
}
