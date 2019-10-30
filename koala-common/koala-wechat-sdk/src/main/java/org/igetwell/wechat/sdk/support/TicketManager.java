package org.igetwell.wechat.sdk.support;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.wechat.sdk.api.TicketAPI;
import org.igetwell.wechat.sdk.ticket.JsTicket;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class TicketManager {

    private static ScheduledExecutorService scheduledExecutorService;

    private static Map<String,String> ticketMap = new ConcurrentHashMap<String,String>();

    private static Map<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<String, ScheduledFuture<?>>();

    private static int poolSize = 2;

    private static boolean daemon = Boolean.TRUE;

    private static String firstAppid;

    private static final String KEY_JOIN = "__";


    /**
     * 初始化 scheduledExecutorService
     */
    private static void initScheduledExecutorService(){
        log.info("daemon:{},poolSize:{}",daemon,poolSize);
        scheduledExecutorService =  Executors.newScheduledThreadPool(poolSize, new ThreadFactory() {
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
        TicketManager.poolSize = poolSize;
    }

    /**
     * 设置线程方式
     * @param daemon daemon
     */
    public static void setDaemon(boolean daemon) {
        TicketManager.daemon = daemon;
    }

    /**
     * 初始化ticket(jsapi) 刷新，每119分钟刷新一次。<br>
     * 依赖TokenManager
     * @param appid appid
     */
    public static void init(final String appid){
        init(appid,0,60*119);
    }

    /**
     * 初始化ticket 刷新，每119分钟刷新一次。<br>
     * 依赖TokenManager
     * @since 2.8.2
     * @param appid appid
     * @param types [jsapi,wx_card]
     */
    public static void init(final String appid,String types){
        init(appid,0,60*119,types);
    }

    /**
     * 初始化ticket(jsapi) 刷新
     * 依赖TokenManager
     * @since 2.6.1
     * @param appid appid
     * @param initialDelay 首次执行延迟（秒）
     * @param delay 执行间隔（秒）
     */
    public static void init(final String appid,int initialDelay,int delay){
        init(appid,initialDelay, delay,"jsapi");
    }

    /**
     * 初始化ticket 刷新
     * 依赖TokenManager
     * @since 2.8.2
     * @param appid appid
     * @param initialDelay 首次执行延迟（秒）
     * @param delay 执行间隔（秒）
     * @param types ticket 类型  [jsapi,wx_card]
     */
    public static void init(final String appid,int initialDelay,int delay,String... types){
        if(firstAppid == null){
            firstAppid = appid;
        }
        for(final String type : types){
            final String key = appid + KEY_JOIN + type;
            if(scheduledExecutorService == null){
                initScheduledExecutorService();
            }
            if(futureMap.containsKey(key)){
                futureMap.get(key).cancel(true);
            }
            //立即执行一次
            if(initialDelay == 0){
                doRun(appid, type, key);
            }
            ScheduledFuture<?> scheduledFuture =  scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    doRun(appid, type, key);
                }
            },initialDelay == 0 ? delay : initialDelay,delay, TimeUnit.SECONDS);
            futureMap.put(key,scheduledFuture);
        }
    }

    private static void doRun(final String appid, final String type, final String key) {
        try {
            String accessToken = TokenManager.getToken(appid);
            JsTicket ticket = TicketAPI.getTicket(accessToken, type);
            ticketMap.put(key,ticket.getTicket());
            log.info("TICKET refurbish with appid:{} type:{}",appid,type);
        } catch (Exception e) {
            log.error("TICKET refurbish error with appid:{} type:{}",appid,type);
            log.error("", e);
        }
    }

    /**
     * 取消 ticket 刷新
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
        destroyed(appid,"jsapi","wx_card");
    }

    /**
     * 取消刷新
     * @param appid appid
     * @param types ticket 类型  [jsapi,wx_card]
     */
    public static void destroyed(String appid,String... types){
        for(String type : types){
            String key = appid + KEY_JOIN + type;
            if(futureMap.containsKey(key)){
                futureMap.get(key).cancel(true);
                log.info("destroyed appid:{} type:{}",appid,type);
            }
        }
    }

    /**
     * 获取 ticket(jsapi)
     * @param appid appid
     * @return ticket
     */
    public static String getTicket(final String appid){
        return getTicket(appid ,"jsapi");
    }


    /**
     * 获取 ticket
     * @param appid appid
     * @param type jsapi or wx_card
     * @return ticket
     */
    public static String getTicket(final String appid,String type){
        return ticketMap.get(appid + KEY_JOIN + type);
    }


    /**
     * 获取第一个appid 的第一个类型的 ticket
     * 适用于单一微信号
     * @return ticket
     */
    public static String getDefaultTicket(){
        return ticketMap.get(firstAppid);
    }
}
