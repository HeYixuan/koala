package org.igetwell.wechat.sdk.support;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.wechat.sdk.api.TokenAPI;
import org.igetwell.wechat.sdk.bean.token.Token;

import java.util.Map;
import java.util.concurrent.*;

/**
 * TokenManager token 自动刷新
 */
@Slf4j
public class TokenManager {

    private static ScheduledExecutorService scheduledExecutorService;

    private static Map<String,String> tokenMap = new ConcurrentHashMap<String,String>();

    private static Map<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<String, ScheduledFuture<?>>();

    private static int poolSize = 2;

    private static boolean daemon = Boolean.TRUE;

    private static String firstAppid;

    /**
     * 初始化 scheduledExecutorService
     */
    private static void initScheduledExecutorService(){
        log.info("daemon:{},poolSize:{}",daemon, poolSize);
        scheduledExecutorService =  Executors.newScheduledThreadPool(poolSize,new ThreadFactory() {

            @Override
            public Thread newThread(Runnable arg0) {
                Thread thread = Executors.defaultThreadFactory().newThread(arg0);
                //设置守护线程
                thread.setDaemon(daemon);
                return thread;
            }
        });
    }

    /**
     * 设置线程池
     * @param poolSize poolSize
     */
    public static void setPoolSize(int poolSize){
        TokenManager.poolSize = poolSize;
    }

    /**
     * 设置线程方式
     * @param daemon daemon
     */
    public static void setDaemon(boolean daemon) {
        TokenManager.daemon = daemon;
    }

    /**
     * 初始化token 刷新，每118分钟刷新一次。
     * @param appid appid
     * @param secret secret
     */
    public static void init(final String appid,final String secret){
        init(appid, secret, 0, 60 * 118);
    }

    /**
     * 初始化token 刷新，每118分钟刷新一次。
     * @param appid appid
     * @param secret secret
     * @param initialDelay 首次执行延迟（秒）
     * @param delay 执行间隔（秒）
     */
    public static void init(final String appid,final String secret,int initialDelay,int delay){
        if(scheduledExecutorService == null){
            initScheduledExecutorService();
        }
        if(firstAppid == null){
            firstAppid = appid;
        }
        if(futureMap.containsKey(appid)){
            futureMap.get(appid).cancel(true);
        }
        //立即执行一次
        if(initialDelay == 0){
            doRun(appid, secret);
        }
        ScheduledFuture<?> scheduledFuture =  scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                doRun(appid, secret);
            }
        },initialDelay == 0 ? delay : initialDelay,delay,TimeUnit.SECONDS);
        futureMap.put(appid, scheduledFuture);
        log.info("appid:{}",appid);
    }

    private static void doRun(final String appid, final String secret) {
        try {
            Token token = TokenAPI.oauthToken(appid, secret);
            tokenMap.put(appid, token.getAccessToken());
            log.info("ACCESS_TOKEN refurbish with appid:{}",appid);
        } catch (Exception e) {
            log.error("ACCESS_TOKEN refurbish error with appid:{}",appid , e);
        }
    }

    /**
     * 取消 token 刷新
     */
    public static void destroyed(){
        scheduledExecutorService.shutdownNow();
        log.info("destroyed");
    }

    /**
     * 取消刷新
     * @param appid appid
     */
    public static void destroyed(String appid){
        if(futureMap.containsKey(appid)){
            futureMap.get(appid).cancel(true);
            log.info("destroyed appid:{}",appid);
        }
    }

    /**
     * 获取 access_token
     * @param appid appid
     * @return token
     */
    public static String getToken(String appid){
        return tokenMap.get(appid);
    }

    /**
     * 获取第一个appid 的 access_token
     * 适用于单一微信号
     * @return token
     */
    public static String getDefaultToken(){
        return tokenMap.get(firstAppid);
    }

}
