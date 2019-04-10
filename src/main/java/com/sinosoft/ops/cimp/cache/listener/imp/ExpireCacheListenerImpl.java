package com.sinosoft.ops.cimp.cache.listener.imp;

import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;
import com.sinosoft.ops.cimp.cache.config.Param;
import com.sinosoft.ops.cimp.cache.listener.CacheListener;
import com.sinosoft.ops.cimp.cache.listener.CacheListenerMap;
import com.vip.vjtools.vjkit.time.ClockUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存过期监听处理器
 */
public class ExpireCacheListenerImpl implements CacheListener, Runnable {

    /**
     * 失效时间单位
     */
    private static final Long EXPIRED_UNIT = 1000L;

    /**
     * 日志输出
     */
    private static final Logger log = LoggerFactory.getLogger(ExpireCacheListenerImpl.class);

    /**
     * 初始化Map，存储缓存大类小类，访问时间，有效时间
     */
    private static ConcurrentHashMap<Object, Object> cacheMap;

    /**
     * 任务调度器
     */
    private ScheduledExecutorService executor;

    /**
     * scheduleTimerTime 任务执行间隔时间 单位毫秒， 默认时间 1S/次
     */
    private Long scheduleTimerTime = EXPIRED_UNIT;

    /**
     * 调度器参数配置中参数名
     */
    private static final String SCHEDULE_TIMER_TIME_PARAM = "scheduleTimerTime";

    private CacheManagerInfo.CacheListeners.CacheListener cacheListenerCfg;

    /**
     * 构造
     */
    public ExpireCacheListenerImpl() {
        scheduleTimer();
    }

    @Override
    public void init(CacheManagerInfo.CacheListeners.CacheListener cacheListenerCfg) {
        log.debug("缓存监听初始化开始...");
        cacheMap = new ConcurrentHashMap<>();
        //得到任务调度的时间
        Long lCacheTimerTimeXml;
        if (cacheListenerCfg.getParams() != null) {
            for (Param param : cacheListenerCfg.getParams().getParam()) {
                if (SCHEDULE_TIMER_TIME_PARAM.equals(param.getName())) {
                    String scheduleTimerTimeInParam = param.getValue();
                    if (StringUtils.isNotEmpty(scheduleTimerTimeInParam)) {
                        lCacheTimerTimeXml = Long.parseLong(scheduleTimerTimeInParam);
                        this.scheduleTimerTime = lCacheTimerTimeXml * EXPIRED_UNIT;
                        if (log.isDebugEnabled()) {
                            log.debug("任务调度频率：" + scheduleTimerTime + "毫秒/每次 ");
                        }
                    }
                }
            }
        }
        // 监听配置信息
        this.cacheListenerCfg = cacheListenerCfg;
    }

    /**
     * 执行的方法
     */
    @Override
    public void run() {
        this.remove();
    }


    /**
     * 记录最新一次时间
     *
     * @param listenerMap 监听缓存对象
     * @return result true 记录成功，false 记录失败
     */
    @Override
    public boolean get(CacheListenerMap listenerMap) {
        if (cacheMap != null) {
            Set entryMap = cacheMap.entrySet();
            long nowTime = ClockUtil.currentTimeMillis();
            for (Object anEntryMap : entryMap) {
                Map.Entry entry = (Map.Entry) anEntryMap;
                CacheListenerMap cacheMapListener = (CacheListenerMap) entry.getKey();
                if (listenerMap.equals(cacheMapListener)) {
                    cacheMap.replace(cacheMapListener, nowTime);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 存储到cacheMap中
     */
    @Override
    public void put(CacheListenerMap listenerMap) {
        if (cacheMap != null && listenerMap != null) {
            long nowTime = ClockUtil.currentTimeMillis();
            if (0L != listenerMap.getExpiryTime()) {
                if (log.isDebugEnabled()) {
                    log.debug("CacheMap新增加对象【" + listenerMap.toString() + "】");
                }
                cacheMap.put(listenerMap, nowTime);
            }
        }
    }

    @Override
    public String getName() {
        if (this.cacheListenerCfg != null) {
            return this.cacheListenerCfg.getListenerName();
        } else {
            return "";
        }
    }

    /**
     * 移除缓存操作
     */
    @Override
    public boolean remove(CacheListenerMap listenerVO) {
        boolean removeResult = false;
        //非过期的自动清理，cacheManger自动调用remove时候
        if (cacheMap.containsKey(listenerVO)) {
            //直接删除
            cacheMap.remove(listenerVO);
            removeResult = true;
        } else {
            //得到cacheMap所有的key
            for (Object o : cacheMap.keySet()) {
                CacheListenerMap cacheListenerMap = (CacheListenerMap) o;
                //cacheMap的key中cacheName与listenerVO的cacheName相匹配
                if (listenerVO.getCacheName().equals(cacheListenerMap.getCacheName())) {
                    //将cacheListenerMapVO 删除
                    cacheMap.remove(cacheListenerMap);
                    removeResult = true;
                }
            }
        }
        return removeResult;
    }

    /**
     * 自动移除相关缓存记录信息
     */
    @SuppressWarnings("unchecked")
    private void remove() {
        //任务调度 删除
        if (cacheMap != null) {
            //遍历cacheMap集合，删除所有
            Set<Map.Entry<Object, Object>> entryCacheMap = cacheMap.entrySet();
            long nowTime = ClockUtil.currentTimeMillis();
            for (Map.Entry<Object, Object> eCacheMap : entryCacheMap) {
                //最近一次访问时间
                long nearTimeOfMap = Long.parseLong(eCacheMap.getValue().toString());
                CacheListenerMap listenerMap = (CacheListenerMap) eCacheMap.getKey();
                Object clearKey = eCacheMap.getKey();
                //有效时长
                long expiryTimeOfMap = listenerMap.getExpiryTime();
                // 删除失效的缓存内容
                if (expiryTimeOfMap <= (nowTime - nearTimeOfMap)) {
                    //移除cacheMap的过期缓存   null 没有移除  !null 移除成功
                    Object removeMapResult = cacheMap.remove(clearKey);
                    if (removeMapResult != null) {
                        //删除成功
                        if (log.isInfoEnabled()) {
                            log.info("CacheMap删除成功：【clearKey】=【" + clearKey + "】，【clearValue】=【" + removeMapResult + "】");
                        }
                    } else {
                        //删除失败
                        if (log.isInfoEnabled()) {
                            log.info("cacheMap删除失败：【clearKey】=【" + clearKey + "】");
                        }
                    }
                    //移除具体缓存
                    CacheManager.getInstance().remove(listenerMap.getCacheName(), listenerMap.getKey());
                }
            }
        }
    }

    @Override
    public void stop() {
        // 停止任务调度
        if (executor != null) {
            executor.shutdown();
        }
        // 清理缓存信息
        if (cacheMap != null) {
            this.remove();
        }
    }

    /**
     * 设置定时任务
     */
    private void scheduleTimer() {
        // 初始化监听器处理线程
        executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            // 线程计数器
            private final AtomicInteger threadNum = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable) {
                String name = getName() + this.threadNum.getAndIncrement();
                Thread thread = new Thread(runnable, name);
                // 后台守护线程，不阻断用户线程的退出
                thread.setDaemon(true);
                return thread;
            }
        });
        // 设置线程执行周期
        executor.scheduleWithFixedDelay(
                this,//当前类
                0,
                this.scheduleTimerTime,
                TimeUnit.MILLISECONDS
        );
    }
}
