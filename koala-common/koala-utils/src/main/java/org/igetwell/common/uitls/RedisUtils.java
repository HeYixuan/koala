package org.igetwell.common.uitls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param expire 时间(秒)
     * @return
     */
    public boolean expire(String key,long expire){
        try {
            if(expire > 0){
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    //============================String  Object=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public String get(String key){
        return key == null ? null : (String) redisTemplate.opsForValue().get(key);
    }

    public Object getObject(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, long expire){
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public void set(String key, Object value, long expire){
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean exist(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta < 0){
            return redisTemplate.opsForValue().increment(key, 1);
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long incr(String key){
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 值
     * @return
     */
    public long decr(String key, long delta){
        if(delta < 0){
            return redisTemplate.opsForValue().decrement(key, 1);
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decr(String key){
        return redisTemplate.opsForValue().decrement(key);
    }



}
