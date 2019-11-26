package org.igetwell.common.uitls;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final static long LOCK_EXPIRE = 30 * 1000L;

    /**
     * 指定缓存失效时间
     * @param key    键
     * @param expire 时间(秒)
     * @return
     */
    public boolean expire(String key, long expire) {
        try {
            if (expire > 0) {
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean exist(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public <V> V get(String key) {
        return key == null ? null : (V) redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        if (key == null || key.length() == 0 || value == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     * @param key    键
     * @param value  值
     * @param expire 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key, Object value, long expire) {
        if (key == null || key.length() == 0 || value == null) {
            return;
        }
        try {
            if (expire > 0) {
                redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <V> V getAndSet(String key, Object value) {
        if (key == null || key.length() == 0 || value == null) {
            return null;
        }
        return (V) redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            return redisTemplate.opsForValue().increment(key, 1);
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递增
     * @param key
     * @return
     */
    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 递减
     * @param key   键
     * @param delta 值
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            return redisTemplate.opsForValue().decrement(key, 1);
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key
     * @return
     */
    public long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }


    /**
     * HashGet
     * @param key   键
     * @param field 域
     * @return 值
     */
    public <V> V hget(String key, String field) {
        return (V) redisTemplate.opsForHash().get(key, field);
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param field 域
     * @param value 值
     */
    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key    键
     * @param field  域
     * @param value  值
     * @param expire 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public void hset(String key, String field, Object value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);
        if (expire > 0) {
            expire(key, expire);
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long hsize(String key) {
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 删除hash表中的值
     * @param key   键
     * @param field 域
     */
    public void hdel(String key, Object... field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * 获取list缓存的所有内容
     * @param key 键
     * @return
     */
    public List<Object> lget(String key) {
        return lget(key, 0, -1);
    }

    /**
     * 获取list缓存的内容
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lget(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     */
    public void lset(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @param expire  时间(秒)
     */
    public void lset(String key, Object value, long expire) {
        redisTemplate.opsForList().rightPush(key, value);
        if (expire > 0) {
            expire(key, expire);
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lsize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object getIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public void listUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /***
     * 加锁
     * @param key
     * @param expire 过期时间 当前时间+超时时间
     * @return 锁住返回true
     */
    public boolean lock(String key,String expire){
        boolean bool = redisTemplate.opsForValue().setIfAbsent(key, expire);
        if(bool){//setNX 返回boolean
            return true;
        }
        // 判断锁超时 - 防止原来的操作异常，没有运行解锁操作 ，防止死锁
        String expireValue = this.get(key);
        if(!StringUtils.isEmpty(expireValue) && Long.parseLong(expireValue) < System.currentTimeMillis()){
            //如果对应的锁已经存在，获取上一次设置的时间戳之后并重置lockKey对应的锁的时间戳
            String oldvalue  = this.getAndSet(key, expire);
            if(!StringUtils.isEmpty(oldvalue) && oldvalue.equals(expireValue)){
                log.info("加锁成功, {}", key);
                return true;
            }
        }
        return false;
    }

    /***
     * 解锁
     * @param key
     * @param value
     * @return
     */
    public void unlock(String key, String value){
        try {
            String currentValue = this.get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {//如果不为空,就删除锁
                this.del(key);
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
        this.del(key);
    }

    //加锁脚本
    private static final String SCRIPT_LOCK = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('pexpire', KEYS[1], ARGV[2]) return 1 else return 0 end";
    // 解锁脚本
    private static final String SCRIPT_UNLOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    public boolean tryLock(String key, int expire) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(SCRIPT_LOCK, Long.class);
        Long val = redisTemplate.execute(script, Collections.singletonList(key), String.valueOf(expire));
        return val == 1;
    }

    public boolean releaseLock(String key) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(SCRIPT_UNLOCK, Long.class);
        Long val = redisTemplate.execute(script, Collections.singletonList(key));
        return val == 1;
    }


    public static void main(String[] args) {
        RedisUtils redisUtils = new RedisUtils();
        String a = redisUtils.get("a");

        Gson o = redisUtils.get("a");
    }
}
